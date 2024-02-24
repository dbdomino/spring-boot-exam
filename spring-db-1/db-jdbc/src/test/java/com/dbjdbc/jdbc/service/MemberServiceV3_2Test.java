package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static com.dbjdbc.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
class MemberServiceV3_2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_2 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME, PASSWORD);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        memberRepository = new MemberRepositoryV3(dataSource);
        memberService = new MemberServiceV3_2(transactionManager, memberRepository); // 트랜잭션 탬플릿, 트랜잭션 매니저 필요함.
    }
    @AfterEach
    void after() throws SQLException {
//        memberRepository.delete(MEMBER_A);
//        memberRepository.delete(MEMBER_B);
//        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransefer() throws SQLException {
        //given
//        Member memberA = new Member(MEMBER_A, 15000);
//        Member memberB = new Member(MEMBER_B, 15000);
//        memberRepository.save(memberA);
//        memberRepository.save(memberB);
        memberRepository.update(MEMBER_A,15000);
        log.warn("MEMBER_A");
        memberRepository.update(MEMBER_B,15000);
        log.warn("MEMBER_B");
        //when
        memberService.accountTransfer(MEMBER_A, MEMBER_B, 5000);
        log.warn("MEMBER_A MEMBER_B");

        //then
        Member findMemberA = memberRepository.findById(MEMBER_A);
        log.warn("MEMBER_A ");
        Member findMemberB = memberRepository.findById(MEMBER_B);
        log.warn("MEMBER_B");
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(20000);
    }

    @Test
    @DisplayName("이체중 예외발생")
    void accountTranseferEx() throws SQLException {
        //given
        memberRepository.update(MEMBER_A,15000);
        memberRepository.update(MEMBER_EX,15000);
        //when
//        assertThatThrownBy(() -> ).isInstanceOf(IllegalStateException.class);  // 해당 예외 터지면 성공
        assertThatThrownBy(() -> memberService.accountTransfer(MEMBER_A, MEMBER_EX, 5000)).isInstanceOf(IllegalStateException.class);


        //then
        Member findMemberA = memberRepository.findById(MEMBER_A);
        Member findMemberB = memberRepository.findById(MEMBER_EX);
        assertThat(findMemberA.getMoney()).isEqualTo(15000); // 현재 롤백 적용이 안되있어서 10000 으로 해줌
        assertThat(findMemberB.getMoney()).isEqualTo(15000);
        // 트랜젝션 걸면 롤백 자동으로 됨. 애플리케이션에서 트랜잭션을 어떤 계층에 걸어야 할까? 쉽게 이야기해서 트랜잭션을 어디에서 시작하고, 어디에서 커밋해야할까?
        /**
         * 방법 1.  리포지토리가 파라미터를 통해 같은 커넥션을 유지할 수 있도록 파라미터를 추가하자. (리포지터리에서 같은 커넥션을 받아서 수행시켜야 하나의 커넥션이 확정되고, 그걸로 Rollback Commit 수행 가능.)
         * V2에서 해결 서비스와 리포지터리에 로직의 추가로 해결
         * 방법 2. 서비스계층의 비즈니스 로직을 분리하고, 트랜잭션 유지하도록 트랜잭션 탬플릿 + 트랜잭션 매니저 사용.
         * V3-1 에선 트랜잭션 매니저로 트랜잭션 적용, V3-2에서 트랜잭션 탬플릿으로 코드 간소화.,
         */
    }
}