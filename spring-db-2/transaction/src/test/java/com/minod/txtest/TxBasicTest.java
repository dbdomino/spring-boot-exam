package com.minod.txtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    TestService testService;

    @Test
    void proxyCheck() {
        // class=class com.minod.txtest.TxBasicTest$TestService  // 메소드에 @Transactional 빼면
        // class=class com.minod.txtest.TxBasicTest$TestService$$SpringCGLIB$$0   // 메소드에 @Transactional 넣으면
        // 트랜잭션 AOP가 @Transactional 가 하나라도 있는 메서드라면, 클래스를 만들 때, 프록시 클래스를 만들어 스프링 컨테이너에 등록함.
        log.info("aop class={}", testService.getClass());
//        assertThat(AopUtils.isAopProxy(testService)).isTrue(); // testService 가 Proxy 거쳐서 생성된건지 확인
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
    @Transactional
    static class TestService {

        public void tx() {
            log.info("call tx");
            // 뭐임이건?(트랜잭션 동기화 매니저) // 트랜잭션 수행중인지 확인
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }

        public void nonTx() {
            log.info("call nonTx");
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive(); // 트랜잭션 수행중인지 확인
            log.info("tx active={}", txActive);
        }

    }


}
