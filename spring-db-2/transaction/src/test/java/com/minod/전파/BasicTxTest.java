package com.minod.전파;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager tm; // 트랜잭션 매니저 주입.

    @TestConfiguration
    static class Config{
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager( dataSource);
        }
    }
    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus status = tm.getTransaction(new DefaultTransactionAttribute()); // 트랜잭션 불러오는 방법

        log.info("트랜잭션 커밋 커밋");
        tm.commit(status);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void double_commit() {
        log.info("트랜잭션1 시작");
        TransactionStatus status1 = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        tm.commit(status1);
        log.info("트랜잭션1 커밋완료");

        log.info("트랜잭션2 시작");
        TransactionStatus status2 = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋");
        tm.commit(status2);
        log.info("트랜잭션2 커밋완료");
        // tm.rollback(status2); // 롤백하는 소스
    }

    @Test
    void double_commit_rollback() {
        log.info("트랜잭션1 시작");
        TransactionStatus status1 = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        tm.commit(status1);
        log.info("트랜잭션1 커밋완료");

        log.info("트랜잭션2 시작");
        TransactionStatus status2 = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋");
        /*tm.commit(status2);
        log.info("트랜잭션2 커밋완료");*/
        tm.rollback(status2); // 롤백하는 소스
        log.info("트랜잭션2 롤백");
    }

    @Test
    void inner_commit() { // 외부트랜잭션 시작 중에 내부 트랜잭션 시작과 commit 수행, 그 후 외부 트랜잭션 commit
        log.info("외부 트랜잭션 시작");
        TransactionStatus outStatus = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outStatus.isNewTransaction()); // outStatus.isNewTransaction()=true
        log.info("내부 트랜잭션 시작");
        TransactionStatus innerStatus = tm.getTransaction(new DefaultTransactionAttribute()); // 내부 트랜잭션이 들어왔다. 그러면 내부 트랜잭션은 newTransaction 상태가 false로 만들어진다.
        log.info("중간확인 outStatus.isNewTransaction()={}", outStatus.isNewTransaction());  // outStatus.isNewTransaction()=true // true는 외부 트랜잭션(status) 하나 뿐이다.
        log.info("중간확인 inner.isNewTransaction()={}", innerStatus.isNewTransaction());  // innerStatus.isNewTransaction()=false // 아 이 트랜잭션 상태로 물리트랜잭션 시작과 종료를 구분하는군.
        log.info("내부 트랜잭션 커밋");
        tm.commit(innerStatus);
//        tm.commit(innerStatus);// 스프링에서 하나의 논리트랜잭션은 두번 이상 commit 못한다.
        log.info("외부 트랜잭션 커밋");
        tm.commit(outStatus);
    }

}
