package com.minod.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    @Transactional // 기본값 Propagation.REQUIRED
//    @Transactional(propagation = Propagation.REQUIRED) // 프로퍼게이션 전파, 트랜잭션이 연달아 들어올 때 처리방법,
    public void save(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    public void saveNoTransactional(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    public Optional<Member> find(String username) { // 조회를 Id가 아닌 username 으로 하는것은 jpql 로 해야한다.
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
//                .getSingleResult()   // getSingleResult는 값이 없으면  NoResultException 에러를 던진다. 필요하면 쓰자
                .getResultList() //  List<Member> 로 반환해 줌.
                .stream().findAny(); // 만약에 결과가 2개이상 나오면, 그중에 값 하나로 


    }
}
