package com.minod.txtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {
    @Autowired LevelService service;

    @Test
    void orderTest() {
        service.write();
        service.read();
    }

    @TestConfiguration
    static class TxApplyLevelConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    // 트랜잭션 active 옵션 / readOnly 옵션 있음.

    @Slf4j
    @Transactional(readOnly = true) // 왜쓰나 readOnly 옵션
    static class LevelService {
        @Transactional(readOnly = false) // false가 기본값
        public void write() {
            log.info("call write");
            printTxInfo();
            log.info("call write end");
        }
        public void read() {
            log.info("call read");
            printTxInfo();
            log.info("call read end");
        }
        private void printTxInfo() {
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            boolean readOnly =
                    TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", readOnly);

        }
    }
}
