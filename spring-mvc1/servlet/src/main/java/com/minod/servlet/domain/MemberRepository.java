package com.minod.servlet.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 싱글톤으로 사용할 Repository
public class MemberRepository {
    private static Map<Long, Member> store = new ConcurrentHashMap<>(); // 싱글톤이라서 해시테이블이나 ConcurrentHashMap 쓴다.
    private static long sequence = 0L; // 동시성 을 위해 AtomicLong 을 사용하는게 나을 수 있다.

    private static final MemberRepository instance = new MemberRepository();

    private MemberRepository() { // 생성자 막는다.

    }
    public static MemberRepository getInstance() { // 인스턴스 가져오도록하기위해
        return instance;
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }
    public Member findById(Long id) {
        return store.get(id);
    }
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

// 테스트위해
    public void clearStore() {
        store.clear();
    }


}
