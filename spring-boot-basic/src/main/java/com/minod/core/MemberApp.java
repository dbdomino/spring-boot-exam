package com.minod.core;

import com.minod.core.config.Appconfig;
import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import com.minod.core.service.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        MemberService memberService = new MemberServiceImpl(); // 멤버서비스 구현체 사용은 인터페이스가 사용하도록 하기

        // 일반적인 의존성 주입
/*
        Appconfig appconfig = new Appconfig();
        MemberService memberService = appconfig.memberService();
*/
        //AnnotationConfigApplicationContext 은 Bean으로 지정된 객체들을 가지고 있는 context 입니다. 컨테이너라고도 부릅니다.
        // ApplicationContext 를 스프링 컨테이너라 한다.
        // 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있다.
        // Appconfig.class의 환경설정 정보에서 spring에 의해 주입된 bean을 담고 있다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
        // getBean(메서드이름, 반환하는 타입); 으로 MemberService 받아오기
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        // SRP(단일 책임 원칙)
        // OCP(개방 폐쇄 원칙)
        // LSP(리스코프 치환 원칙)
        // ISP(인터페이스 분리 원칙)
        // DIP(의존관계 역전 원칙)



        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);
//        Member member1 = new Member(1L, "memberA이름", Grade.VIP);
        Member member1 = memberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + member1.getName());

    }
}
