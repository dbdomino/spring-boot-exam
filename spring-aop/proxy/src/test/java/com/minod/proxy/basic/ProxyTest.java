package com.minod.proxy.basic;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
public class ProxyTest {
    @Autowired
    TestService testService;

    @Test
    void proxyCheck() {
        // class=class com.minod.txtest.TxBasicTest$TestService  // tx() 메소드에 @Transactional 빼면 바로 테스트서비스 바라봄
        // class=class com.minod.txtest.TxBasicTest$TestService$$SpringCGLIB$$0   // 메소드에 @Transactional 넣으면 프록시를 바라봄,
        // 트랜잭션 AOP가 @Transactional 가 하나라도 있는 메서드라면, 클래스를 만들 때, 프록시 클래스를 만들어 스프링 컨테이너에 등록함.
        log.info("aop class={}", testService.getClass());
        log.info("aop class주소={}", testService);
        testService.tx();  // tx수행시 내부에 구현된 로그로 this.getClass() 해보면, 인스턴스 형식에 CGLIB이 안붙어있음. 객체 주소값은 같음. 즉 프록시에 testService가 그대로 복사되어 구현된게 아니라
        // 프록시에 추가 기능이 구현되고, 기존 기능은 구현체를 참조하여 실행하는 식으로 구현되었다.
        assertThat(AopUtils.isAopProxy(testService)).isTrue(); // testService 가 Proxy 거쳐서 생성된건지 확인
    }
    @Test
    void txTest() {
        testService.tx();
        testService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        TestService testService() {
            return new TestService();
        }
    }

    @Slf4j
    static class TestService {
        @Transactional
        public void tx() {
            log.info("call tx");
            // 뭐임이건?(트랜잭션 동기화 매니저) // 트랜잭션 수행중인지 확인
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            log.info("tx get class={}", this.getClass()); // 이건 @Transactional이 붙어 있어도,
            log.info("tx get class주소={}", this); // 이건 @Transactional이 붙어 있어도,
        }

        public void nonTx() {
            log.info("call nonTx");
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive(); // 트랜잭션 수행중인지 확인
            log.info("tx active={}", txActive);
        }

    }

}
