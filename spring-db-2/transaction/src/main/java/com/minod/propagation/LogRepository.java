package com.minod.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {
//    @PersistenceContext // 영속성컨텍스트 어노테이션 넣어줘야 하나, 스프링 버전없되면서 사라졌다고 함.
    private final EntityManager em;

    @Transactional
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(LogFactory logMessage) {
        log.info("log 저장");
        em.persist(logMessage);
        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)   // Propagation.REQUIRES_NEW 를 쓰면 해당 트랜잭션은 별개의 커넥션에서 별개의 트랜잭션으로 처리되므로, 여기서 실패한다 해도, 기존 트랜잭션에 영향을 주지 않는다.
    public void saveREQUIRES_NEW(LogFactory logMessage) {
        log.info("log 저장");
        em.persist(logMessage);
        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public void saveNoTransactional(LogFactory logMessage) {
        log.info("log 저장");
        em.persist(logMessage);
        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<LogFactory> find(String message) {
        return em.createQuery("select l from LogFactory l where l.message = :message", LogFactory.class)
                .setParameter("message", message)   // 조회하는 쿼리
                .getResultList().stream().findAny();
    }



}
