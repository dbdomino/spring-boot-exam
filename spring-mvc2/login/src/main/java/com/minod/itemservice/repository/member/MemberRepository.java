package com.minod.itemservice.repository.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    public Member save (Member member){
        member.setId(++sequence);
        log.info("save : member= {}" + member);

        store.put(member.getId(), member);
        log.info("save : complete= {}" + member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }
    public Optional<Member> findByLoginId(String loginId) {  // 왜 String Id로 찾지
        // 로그인Id를 멤버에서 왜찾는데 Member에 id를 LoginId로 변수명을 지정해놔서 그런거, 해깔리는데,, 로그인한 아이디인줄
        /*List<Member> all= findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();*/

        return findAll().stream()
                .filter( (m) -> { return m.getLoginId().equals(loginId);})  // 조건
//                .findAny();
                .findFirst();

    }

    public List<Member> findAll() {
        // public Collection<V> values() , 사용하기위해 아래처럼 멀로쓸건지 지정 해줘야댄다.
        // 꼭 values를 꺼내기 위해서 쓰는 양식이다.
        return new ArrayList<>(store.values());
    }



}
