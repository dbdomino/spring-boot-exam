package com.minod.txtest;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxInitTest {
    @Autowired Hello hello;

    @Test
    void test01() {
        // initV1은 @PostConstruct 때문에 스프링 빈 등록시도 호출되는데, 빈 등록시(초기화 시) AOP가 트랜잭션 지원안해준다고 함.
//        hello.initV1();// 직접 메소드 호출은 당연히 트랜잭션 적용된다.

//        log.info("callService class={}", callService.getClass());
    }


    @TestConfiguration
    static class InitV1Config {
        @Bean
        Hello hello() {
            return new Hello();
        }  // 이것만 있어도 Bean 초기화 가능
        // 초기화 시에 트랜잭션이 동작 안할 수 있다.
    }

    @Slf4j
    static class Hello {
        @PostConstruct // 스프링 컨테이너에 스프링 빈으로 등록할 때 실행됨.
        @Transactional // 초기화 코드, 예를 들어 @PostConstruct와 같이 사용하면 트랜잭션 적용 안됨. 초기화 코드가 수행된 이후 트랜잭션 AOP가 적용되기 때문이다.
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active={}", isActive); // 트랜잭션 활성화 유무
        }

        @EventListener(value = ApplicationReadyEvent.class)
        @Transactional // AOP만든 다음에, Bean으로 등록 후 실행하면, 초기화 하면서 실행하는 거랑 비슷해서, 트랜잭션 보장되는 상태로 메서드 실행 가능함.
        // 스프링, AOP 라이프 사이클을 모르면 수행이 불가능함.
        public void init2() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @EventListener tx active={}", isActive); // 트랜잭션 활성화 유무
        }

    }

}
