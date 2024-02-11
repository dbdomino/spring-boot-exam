package com.minod.core.service;

import com.minod.core.member.Member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long memberId);
}