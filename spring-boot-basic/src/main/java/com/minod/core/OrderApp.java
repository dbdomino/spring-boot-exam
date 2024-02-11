package com.minod.core;

import com.minod.core.config.Appconfig;
import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import com.minod.core.order.Order;
import com.minod.core.service.MemberService;
import com.minod.core.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {
        //AnnotationConfigApplicationContext 은 Bean으로 지정된 객체들을 가지고 있는 context 입니다. 컨테이너라고도 부릅니다.
        // Appconfig.class의 환경설정 정보에서 spring에 의해 주입된 bean을 담고 있다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
        // getBean(메서드이름, 반환하는 타입); 으로 MemberService 받아오기
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);
/*
        Appconfig appconfig = new Appconfig();
        MemberService memberService = appconfig.memberService();
        OrderService orderService = appconfig.orderService();
*/

//        MemberService memberService = new MemberServiceImpl(); // 멤버서비스 구현체 사용은 인터페이스가 사용하도록 하기
//        OrderService orderService = new OrderServiceImpl();

        // SRP(단일 책임 원칙) 한 클래스는 하나의 책임만 가져야 한다
            // 클라이언트 객체는 직접 구현 객체를 생성하고, 연결하고, 실행하는 다양한 책임을 가지고 있는걸 각각 분리하여 구현객체는 Appcomfig, 클라이언트 객체는 실행책임만 담당
        // OCP(개방 폐쇄 원칙) 소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다
            // 다형성 사용하고 클라이언트가 DIP를 지킴, 이덕에 클라이언트 코드는 변경 안해도 됨. -> 요소가 확장되어도 변경은 닫혀 있다.
        // LSP(리스코프 치환 원칙)
        // ISP(인터페이스 분리 원칙)
        // DIP(의존관계 역전 원칙)   추상화에 의존해야지, 구체화에 의존하면 안된다. 의존성 주입은 이 원칙을 따르는 방법 중 하나다
            // AppConfig가 FixDiscountPolicy 객체 인스턴스를 클라이언트 코드 대신 생성해서 클라이언트 코드에 의존관계를 주입했다. 이렇게해서 DIP 원칙을 따르면서 문제도 해결했다.

        /* 스프링 핵심기술
        IoC (Inversion Of Control) 제어의 역전
        DI (의존관계 주입)   DIP를 지키도록 추상화만 바라보면, 구체화가 무엇인지 모른다. 몰라도 사용되게 하는게 의존관계주입.
        의존관계는 정적인 클래스 의존 관계와, 실행 시점에 결정되는 동적인 객체(인스턴스) 의존 관계 둘을 분리해서 생각해야 한다.
        정적 클래스 의존관계( import문 보고 어떤게 의존되는지 알수 있음)
        동적 클래스 의존관계( 실행시켜 보지 않으면 어떤게 의존되는지 알 수 없음. 혹은 의존을 담당하는 소스를 직접 봐야함.)
        애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결 되는 것을 의존관계 주입이라 한다.
        AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 IoC 컨테이너 또는 DI 컨테이너라 한다.

        AOP
        PSA

         */


        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        Member member1 = new Member(memberId, "memberA이름", Grade.VIP);

        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 4900 );

        System.out.println("new order = " + order);
        System.out.println("member = " + member.getName());

    }
}
