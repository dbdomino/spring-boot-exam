package com.dbjdbc.jdbc.service;

import com.dbjdbc.domain.Member;
import com.dbjdbc.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - DataSource TransactionManager 자동주입 확인하기
 */
@Slf4j
@SpringBootTest // AOP 실행을 위해 스프링컨테이너 에서 실행이 필요. 조건1
class MemberServiceV3_4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    // 스프링 Bean에서 끌어오기 위해 컴포넌트 스캔 등록해줘야함. 또는 Bean등록 필요. 조건2
    @Autowired private MemberRepositoryV3 memberRepository;
    @Autowired private MemberServiceV3_3 memberService;

    // 이건 신기하네 처음본다.
    @TestConfiguration // BeforeEach 에 있는거 설정파일로 만들어 Bean으로 등록
    static class TestConfig {

        private final DataSource dataSource;

        public TestConfig(DataSource dataSource){
            this.dataSource = dataSource; // 외부 주입 되도록 생성자 주입
        }
        // dataSource와 transactionManager @Bean으로 등록 없어도 자동으로 주입이 지원된다. 자동생성 지원 되네!!! 오오
        @Bean
        MemberRepositoryV3 memberRepositoryV3() {
            return new MemberRepositoryV3(dataSource);
        } // 외부주입되는거사용
        @Bean
        MemberServiceV3_3 memberServiceV3_3() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }
    }

    @BeforeEach
    void before() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME, PASSWORD);
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

//        memberRepository = new MemberRepositoryV3(dataSource);
//        memberService = new MemberServiceV3_3(memberRepository);
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
        assertThat(findMemberA.getMoney()).isEqualTo(15000); // 서비스에 @Transactional 이 있으면 해결 가능한 문제.
        assertThat(findMemberB.getMoney()).isEqualTo(15000);
        // 트랜젝션 걸면 롤백 자동으로 됨. 애플리케이션에서 트랜잭션을 어떤 계층에 걸어야 할까? 쉽게 이야기해서 트랜잭션을 어디에서 시작하고, 어디에서 커밋해야할까?
        /**
         * 방법 1.  리포지토리가 파라미터를 통해 같은 커넥션을 유지할 수 있도록 파라미터를 추가하자. (리포지터리에서 같은 커넥션을 받아서 수행시켜야 하나의 커넥션이 확정되고, 그걸로 Rollback Commit 수행 가능.)
         * V2에서 해결 서비스와 리포지터리에 로직의 추가로 해결
         * 방법 2. 서비스계층의 비즈니스 로직을 분리하고, 트랜잭션 유지하도록 트랜잭션 탬플릿 + 트랜잭션 매니저 사용.
         * V3-1 에선 트랜잭션 매니저로 트랜잭션 적용, V3-2에서 트랜잭션 탬플릿으로 코드 간소화.,
         * 방법 3. AOP 를 활용한 트랜잭션 적용, @Transactional 붙여 서비스에 구현함. 그러나 rollback시도는 안되는걸로 확인됨.
         *     AOP를 활용하려면 스프링 컨테이너 사용이 반드시 필요하다. 스프링 Bean으로 등록해서 Repository, Service로 의존관계를 Autowired로 Bean으로 주입받아서 써야한다고 함.
         *     또한, 테스트클래스에 스프링 컨테이너없이 단순 @Test로 실행하는거면 AOP 수행 불가능해서 @SpringBootTest넣어줘야 한다고 함.
         */
    }

    @Test
    void AopCheck() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberRepository class={}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue(); // AopUtils ?? 이런것도있네, 소스에 @Transactional이 있으면 AOP Proxy를 만들어서 적용해준다는 소리이므로 true
        // memberService 클래스 정보보면 CGLIB라고 붙은건데, proxy가 주입된 소스로 만들어진 class라는 걸 알 수 있다. class=class com.dbjdbc.jdbc.service.MemberServiceV3_3$$SpringCGLIB$$0
        // 즉 서비스 패키지에 존재하는 소스를 상속받은 AOP 프록시가 주입한 소스로 자동으로 만든 class로 구현된걸 사용한다는 소리다.
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }
}