package com.minod.core.singleton;

import com.minod.core.config.Appconfig;
import com.minod.core.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {

        Appconfig appConfig = new Appconfig();
//1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
//2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();
//참조값이 다른 것을 확인, 호출할때마다 다른 객체를 새로 생성한다.
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
//memberService1 != != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("스프링 없는 순수한 테스트")
    void pure() {
//        SingletonService singletonService = new SingletonService();

    }

    @Test
    @DisplayName("스프링 없는 싱글턴 컨테이너")
    void SingletonContainer() {

        Appconfig appConfig = new Appconfig();
//1. 조회: 호출할 때 마다 객체를 생성
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();
//2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();
//참조값이 다른 것을 확인, 호출할때마다 다른 객체를 새로 생성한다.
        System.out.println("memberService1 = " + singletonService1);
        System.out.println("memberService2 = " + singletonService2);
//memberService1 != != memberService2
        assertThat(singletonService1).isSameAs(singletonService2);
//        assertThat(singletonService1).isNotSameAs(singletonService2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void spirngContaioner() {
//        Appconfig appConfig = new Appconfig();
        ApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        /*
        스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
        컨테이너는 객체를 하나만 생성해서 관리한다.
        스프링 컨테이너는 싱글톤 컨테이너 역할을 한다.
        스프링 컨테이너의 이런 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용할 수 있다.

        주의점
        싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤
        방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
        무상태(stateless)로 설계해야 한다!
        - 특정 클라이언트에 의존적인 필드가 있으면 안된다.(모든 클라이언트가 읽기만 하는 bean 이 되어야한다. bean에 먼가 클라이언트가 상태를 변경시키거나 하면 안된다.)
        - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다!
        - 가급적 읽기만 가능해야 한다.
        - 필드 대신에 자바에서 공유되지 않는 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
        - 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다!!!
         */


//1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

//참조값이 다른 것을 확인, 호출할때마다 다른 객체를 새로 생성한다.
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

//memberService1 != != memberService2
        assertThat(memberService1).isSameAs(memberService2);
//        assertThat(singletonService1).isNotSameAs(singletonService2);
    }

}
