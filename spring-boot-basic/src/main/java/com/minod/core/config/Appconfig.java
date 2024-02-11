package com.minod.core.config;

import com.minod.core.discount.DiscountPolicy;
import com.minod.core.discount.RateDiscountPolicy;
import com.minod.core.repository.MemberRepository;
import com.minod.core.repository.MemoryMemberRepository;
import com.minod.core.service.MemberService;
import com.minod.core.service.MemberServiceImpl;
import com.minod.core.service.OrderService;
import com.minod.core.service.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Appconfig {
// bean으로 등록할 때 의존관계의 순서는 스프링에서 알아서 설정해준다. (의존관계 자동 주입 이라고 함)

    // BeanFactory와 ApplicationContext
    /*
    BeanFactory 스프링 컨테이너의 최상위 인터페이스다. 스프링 빈을 관리하고 조회하는 역할을 담당한다. getBean() 을 제공한다.

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
    ApplicationContext   BeanFactory 기능을 모두 상속받아서 제공한다.

    빈을 관리하고 검색하는 기능을 BeanFactory가 제공해주는데, 그러면 둘의 차이가 뭘까?
    애플리케이션을 개발할 때는 빈을 관리하고 조회하는 기능은 물론이고, 수 많은 부가기능이 필요하다.
    ApplicatonContext가 제공하는 부가기능 때문에 ApplicatonContext를 주로 사용한다.
    메시지소스를 활용한 국제화 기능 -예를 들어서 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력
    환경변수 - 로컬, 개발, 운영등을 구분해서 처리
    애플리케이션 이벤트 - 이벤트를 발행하고 구독하는 모델을 편리하게 지원
    편리한 리소스 조회 - 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회

     */


    /* 의문.
    memberService 빈을 만드는 코드를 보면 memberRepository() 를 호출한다.
이 메서드를 호출하면 new MemoryMemberRepository() 를 호출한다.
orderService 빈을 만드는 코드도 동일하게 memberRepository() 를 호출한다.
이 메서드를 호출하면 new MemoryMemberRepository() 를 호출한다.
결과적으로 각각 다른 2개의 MemoryMemberRepository 가 생성되면서 싱글톤이 깨지는 것 처럼 보인다. 스프링 컨테이너는 이 문제를 어떻게 해결할까?
     */
    @Bean
    public MemberService memberService() {
        System.out.println("call Appconfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call Appconfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService() {
        System.out.println("call Appconfig.orderService");
        return new OrderServiceImpl( memberRepository(), discountPolicy());
//        return null;
    }
    @Bean
    public DiscountPolicy discountPolicy(){
//        return new FixDiscountPolicy();
        System.out.println("call Appconfig.discountPolicy");
        return new RateDiscountPolicy();
    }

}
