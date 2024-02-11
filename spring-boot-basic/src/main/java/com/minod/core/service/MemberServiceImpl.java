package com.minod.core.service;

import com.minod.core.member.Member;
import com.minod.core.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 자동으로 스프링Bean으로 등록하는 방법, 의존성주입은 아래에 별도로 설정해줘야한다, 생성자주입, 필드주입
public class MemberServiceImpl implements MemberService{

    // 읜존성 주입하는게아니면 구현체 넣어줘야한다.
//    private final MemberRepository memberRepository = new MemoryMemberRepository(); // 쓰다가 컨트롤 쉬프트 앤터 하면 자동완성

    //생성자 주입을 통한 의존성 주입으로 DI를 지키려고함.
    private final MemberRepository memberRepository; // 쓰다가 컨트롤 쉬프트 앤터 하면 자동완성

//    public MemberServiceImpl() { // 디폴트생성자 새로 생성하니, private final MemberRepository memberRepository; 랑 부딛치는구나....
//    }

    @Autowired // ac.getBean(MemberRepository.calss) 이런식으로 동작함.
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트용, new를 Appconfig.class 에서 자주하는데 싱글턴깨지는거 아닌지 테스트하는 것.
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
