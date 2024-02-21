package com.minod.itemservice.domain.login;

import com.minod.itemservice.repository.member.Member;
import com.minod.itemservice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
    * return이 null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        /*Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();

        if (member.getPassword().equals(password)) return member;
        else return null;*/
        // Optional로 받으면 Stream기능의 filter를 바로 쓸 수 있다.
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);


    }
}
