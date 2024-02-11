package com.minod.core.singleton;

import com.minod.core.config.Appconfig;
import com.minod.core.repository.MemberRepository;
import com.minod.core.service.MemberServiceImpl;
import com.minod.core.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {
    @Test
    void configurationTest() {
        ApplicationContext ac = new
                AnnotationConfigApplicationContext(Appconfig.class);
        MemberServiceImpl memberService = ac.getBean("memberService",      MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService",         OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
        //모두 같은 인스턴스를 참고하고 있다.
        System.out.println("memberService -> memberRepository = " +
                memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository  = " +
                orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);
        // Appconfig에서 모두 memberRepository() 를 호출하면 new가 3번 뜰거같은데, 왜 같은 인스턴스를 참조하는 걸까?
        // 스프링에서 자동으로 의존관계 주입하고, 의존관계 상위순서도 자동으로 잡아주기 때문에 인스턴스1개만 필요한 것을 자동으로 찾아서 설정해주기 때문이다.
        // Appconfig 에서 로그찍어서 확인해바도 call Appconfig.memberRepository 한번만 뜬다. 진짜로 스프링이 bean으로 생성한건 싱글톤이 되도록 보장해준다....

        //모두 같은 인스턴스를 참고하고 있다.
        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);


    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
//AppConfig도 스프링 빈으로 등록된다.
        Appconfig bean = ac.getBean(Appconfig.class);
        System.out.println("bean = " + bean.getClass()); // bean = class com.minod.core.config.Appconfig$$SpringCGLIB$$0
        //bean = class com.minod.core.config.Appconfig$$SpringCGLIB$$0
        // 이런식으로 뜨는데, 순수한 클래스라면 다음과 같이 출력되어야 한다.
        //class hello.core.AppConfig
        //그런데 예상과는 다르게 클래스 명에 xxxCGLIB가 붙으면서 상당히 복잡해진 것을 볼 수 있다. 이것은 내가 만든 클래
        //스가 아니라 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서
        // AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것이다!
        //
        // 그 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다. 아마도 다음과 같이 바이트 코드를 조작해서 작성되어 있을 것이다.
        // 이는 @Configuration이 들어갔기 때문에 가능한 일이다.
        // bean = class com.minod.core.config.Appconfig
        // @Configuration 빼면 위처럼 나오며, call memberRepository도 3번이 호출되는걸 확인할 수 있다.
        // @Bean만 등록되어도 동작은 정상적으로 하며, 스프링 Bean으로 등록은 되나, 싱글톤 보장은 안된다.
        // 싱글톤 보장되게하려면, 스프링 설정정보로 Bean 등록하는 곳에 @Configuration을 사용하자.


    }
}
