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
public class TxInternalCriticalV2Test {
    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }
    /*@Test
    void internalCall() {  // tx active=true
        callService.internal();
    }*/
    @Test
    void externalCallV2() { // tx active=false
        callService.external();
    }
    @TestConfiguration
    static class InternalCallV1Config {
        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService() {
            return new InternalService();
        }
    }

    @Slf4j
    static class CallService {
        // 분리한 Internal 메소드를 사용하려면, Internal 메소드가 들은 Internalclass를 주입 받아 써야 한다.
        private final InternalService internalService;

        public CallService(InternalService internalService){
            this.internalService = internalService;
        }

        public void external() {
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo() {
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

    @Slf4j
    static class InternalService {
        @Transactional // 이걸 다른 클래스로 분리하여 사용한다.
        // private 일 경우 예외는 안나지만 트랜잭션 처리 안됨.
        // 나머지는 트랜잭션 처리 됨.
        protected void internal() {
            log.info("call internal");
            printTxInfo(); // ??
        }

        private void printTxInfo() {
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

}
