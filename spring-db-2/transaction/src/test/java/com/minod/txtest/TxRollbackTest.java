package com.minod.txtest;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class TxRollbackTest {
    @Autowired RollbackService service;

    @Test
    void RuntimeException() {
        Assertions.assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);

        log.info("ing2 RuntimeException");
        log.info("end RuntimeException");
    }

    @Test
    void checkedException() {

        Assertions.assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor() {
        Assertions.assertThatThrownBy(() -> service.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @Slf4j
    @TestConfiguration // 중요
    static class RollbackConfig {
        @Bean
        public RollbackService rollbackService() {
            return new RollbackService();
        }
    }


    @Slf4j
    static class RollbackService {
        @Transactional
        public void runtimeException() { // 런타임 익셉션으로 던지기?!
            log.info("call RuntimeException");
            throw new RuntimeException("롤백서비스.런타임익셉션()");
//            log.info("end RuntimeException");
        }

        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException("롤백서비스.MyException() 커스텀");
        }

        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor   롤백for 옵션 사용하기");
            throw new MyException("롤백서비스.rollbackFor() 커스텀");
        }
    }

    static class MyException extends Exception {
        public MyException() {
            super();
        }
        public MyException(String s) {
            super(s);
        }
        // 단순 사용자지정 exception
    }
}
