package com.minod.core.repository;

import com.minod.core.member.Member;

import java.util.HashMap;
import java.util.Map;

// db없이도 개발하기위해 만드는 메모리 리포지터리
//@Component
public class MemoryMemberRepository implements MemberRepository{

    // 동시성 이슈를 방지하려면, 컨커런트해시맵 써야한다.
    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
