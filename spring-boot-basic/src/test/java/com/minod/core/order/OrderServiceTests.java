package com.minod.core.order;

import com.minod.core.AutoAppConfig;
import com.minod.core.config.Appconfig;
import com.minod.core.discount.RateDiscountPolicy;
import com.minod.core.member.Grade;
import com.minod.core.member.Member;
import com.minod.core.repository.MemberRepository;
import com.minod.core.repository.MemoryMemberRepository;
import com.minod.core.service.MemberService;
import com.minod.core.service.OrderService;
import com.minod.core.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTests {
    //Appconfig 쓰기전 직접 구현
//    MemberService memberService = new MemberServiceImpl(); // 멤버서비스 구현체 사용은 인터페이스가 사용하도록 하기
//    OrderService orderService = new OrderServiceImpl();

//    Appconfig appconfig = new Appconfig();

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

    MemberService memberService;
    OrderService orderService;
// Appconfig 를 통해 의존성주입, DIP 지키기
    @BeforeEach   // 위에 바로 선언해주는 것보다 BeforeEach로 해주는게 더 낫다.  테스트를 위해서.
    public void beforeEach() {
        Appconfig appconfig = new Appconfig();
        this.memberService = appconfig.memberService();
        this.orderService = appconfig.orderService();
    }

    @Test
    void createOrder() {
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        Member member1 = new Member(memberId, "memberA이름", Grade.VIP);

        memberService.join(member);


        Order order = orderService.createOrder(memberId, "itemA", 4900 );
        assertThat(order.getDiscountPrice()).isEqualTo(490);

        System.out.println("new order = " + order);
        System.out.println("member = " + member.getName());



    }

    @Test
    @DisplayName("필드주입 의존성주입 테스트")
    void findInjectionTest1() {
//        OrderServiceImpl orderService = new OrderServiceImpl(); // 생성자 임의로 만든다고 해도, 제대로 memberRepository에 주입이 안된다. 필드인젝션 한계, Setter주입도 안됨.
//        // 생성자를 이용해 객체를 만드는게아니라, Bean팩토리를 이용해서 싱글턴으로 getBean()해서 쓴거로 사용해야 의존성주입 된걸 느낄수있다.
//        orderService.createOrder(1L, "ItamA", 10000);

    }

    @Test
    @DisplayName("필드주입 의존성주입 테스트2, 수동으로 주입")
    void findInjectionTest2() {
        MemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L,"name", Grade.VIP));


        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new RateDiscountPolicy()); // 생성자 임의로 만든다고 해도, 제대로 memberRepository에 주입이 안된다. 필드인젝션 한계
        // 생성자를 이용해 객체를 만드는게아니라, Bean팩토리를 이용해서 싱글턴으로 getBean()해서 쓴거로 사용해야 의존성주입 된걸 느낄수있다.
        // 근데 누군가 실수로 ComponentScan으로 사용시킬 빈에 위처럼 생성자를 별도로 넣어 사용했다.
        Order order = orderService.createOrder(1L, "ItamA", 10000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }

    @Test
    @DisplayName("OrderService createOrder 테스트")
    void OrderTest() {
//        orderService = ac.getBean(OrderService.class); // 이거쓰면 에러남

        orderService.createOrder(1L, "ItamA", 10000);

        System.out.println(orderService.createOrder(1L, "ItamA", 10000));

    }
}
