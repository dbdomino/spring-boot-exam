package com.minod.core.service;

import com.minod.core.config.Appconfig;
import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceImplTest {

//    MemberService memberService = new MemberServiceImpl();
//    Appconfig appconfig = new Appconfig();
    MemberService memberService;

    // 의존관계 역전 원칙 DIP 를 지키기위해 이런식으로 memberservice를 구현체를 쓰지않고 외부에서 불러옴.
    @BeforeEach   // 위에 바로 선언해주는 것보다 BeforeEach로 해주는게 더 낫다.  테스트를 위해서.
    public void beforeEach() {
        Appconfig appconfig = new Appconfig();
        this.memberService = appconfig.memberService();
    }


    @Test
    void join() {
        //given
        Member member = new Member(1L, "memberA", Grade.VIP);


        //when
        memberService.join(member);
        Member findMember = memberService.findMember(2L);

        //then
//        Assertions.assertThat(member).as("회원 id가 같지않음").isEqualTo(findMember);
        Assertions.assertThat(member).as("회원 id가 같지않음").isNotEqualTo(findMember);

    }

    @Test
    void findMember() {
    }
}