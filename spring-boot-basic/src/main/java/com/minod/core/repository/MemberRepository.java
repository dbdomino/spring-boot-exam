package com.minod.core.repository;

import com.minod.core.member.Member;

public interface MemberRepository {
    void save(Member member);

    Member findById(Long memberId);
}
