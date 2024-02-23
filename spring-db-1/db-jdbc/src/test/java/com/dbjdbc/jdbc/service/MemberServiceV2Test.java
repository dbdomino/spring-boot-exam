package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.dbjdbc.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
/*
커넥션 하나로 트랜잭션 관리하기
 */
@Slf4j
class MemberServiceV2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private  DataSource dataSource;
    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    /**
     * @BeforeEach와 @BeforeAll의 차이
     * @BeforeEach와 @BeforeAll는 실행 시점에 차이가 있다. 둘 모두 테스트 시작 전에 실행되는 것은 같지만,
     * @BeforeEach는 개별 테스트 실행 전에 실행되며,
     * @BeforeAll은 모든 테스트 실행 전에 한 번만 실행된다.
     */
    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2(dataSource, memberRepository);

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
         * 남은 문제
         * 애플리케이션에서 DB 트랜잭션을 적용하려면 서비스 계층이 매우 지저분해지고, 생각보다 매우 복잡한 코드를 요구한다.
         * 추가로 커넥션을 유지하도록 코드를 변경하는 것도 쉬운 일은 아니다. 다음 시간에는 스프링을 사용해서 이런 문제들을 하나씩 해결해보자.
         */
    }
}