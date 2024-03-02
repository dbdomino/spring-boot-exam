package com.minod.전파;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
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

    @Test
    void outer_rollback() { // 외부트랜잭션 롤백시 시작점으로 돌아감.
        log.info("외부 트랜잭션 시작");
        TransactionStatus outStatus = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outStatus.isNewTransaction()); // outStatus.isNewTransaction()=true

        log.info("내부 트랜잭션 시작");
        TransactionStatus innerStatus = tm.getTransaction(new DefaultTransactionAttribute()); // 내부 트랜잭션이 들어왔다. 그러면 내부 트랜잭션은 newTransaction 상태가 false로 만들어진다.
        log.info("내부 트랜잭션 커밋");
        tm.commit(innerStatus);

        log.info("외부 트랜잭션 롤백");
        tm.rollback(outStatus);
    }

    @Test
    void innerr_rollback() { // 외부트랜잭션 롤백시 시작점으로 돌아감.
        log.info("외부 트랜잭션 시작");
        TransactionStatus outStatus = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outStatus.isNewTransaction()); // outStatus.isNewTransaction()=true

        log.info("내부 트랜잭션 시작");
        TransactionStatus innerStatus = tm.getTransaction(new DefaultTransactionAttribute()); // 내부 트랜잭션이 들어왔다. 그러면 내부 트랜잭션은 newTransaction 상태가 false로 만들어진다.
        log.info("내부 트랜잭션 롤백");
        tm.rollback(innerStatus);

        log.info("외부 트랜잭션 커밋");
        tm.commit(outStatus);
    }

    @Test
    void innerr_rollback_requires_new() { // 내부트랜잭션을 별개 트랜잭션으로 분리해서 보장.
        log.info("외부 트랜잭션 시작");
        TransactionStatus outStatus = tm.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outStatus.isNewTransaction()); // outStatus.isNewTransaction()=true

        log.info("내부 트랜잭션 시작");
        // 트랜잭션 분리
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute(); // 머냐이건? 트랜잭션 분리하는 TransactionAttribute
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); //내부 트랜잭션을 시작할 때 전파 옵션인 propagationBehavior 에 PROPAGATION_REQUIRES_NEW 옵션을 주었다.
        log.info("innerStatus.isNewTransaction() 1 =");
        // 트랜잭션 분리 선언을 위해 전파 옵션 설정 후 트랜잭션을 맞이해야 한다. 안그럼 내부 트랜잭션이 포함 되어버림. -> 물리트랜잭션에 포함.
        // getTransaction(안에 분리를 위해 만들어둔 transactionAttribute를 넣어주는게 핵심, new 로 새로 만들어버리면 기본적으로 포함이 되어버림. -> 물리트랜잭션에 포함)
        TransactionStatus innerStatus = tm.getTransaction(transactionAttribute);
        log.info("innerStatus.isNewTransaction() 2 ={}", innerStatus.isNewTransaction());

        log.info("내부 트랜잭션 롤백");
        tm.rollback(innerStatus);

        log.info("외부 트랜잭션 커밋");
        tm.commit(outStatus);
    }

}
