package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

// 애노테이션이 사용된 클래스 기준 판단하는 포인트컷 이라는 소리다...

// @target : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
// @within : 주어진 애노테이션이 있는 타입 내 조인 포인트

/** @target , @within 은 다음과 같이 타입에 지정한 애노테이션이 선언되었다면 AOP 적용 여부를 판단한다.
 @target(hello.aop.member.annotation.ClassAop)        어노테이션 정보가 괄호에 들어간다.
 @target 은 인스턴스의 모든 메서드를(부모 타입의 메서드 포함) 조인 포인트로 적용한다.

 @within(hello.aop.member.annotation.ClassAop)
 @within 은 해당 타입 내에 있는 메서드만(부모타입의 메서드 제외) 조인 포인트로 적용한다.

 참고
 @target , @within 지시자는 뒤에서 설명할 파라미터 바인딩에서 함께 사용된다.
 args, @args, @target , @within 같은 지시자는 단독으로 사용하면 안된다.
 args , @args , @target 은 실제 객체 인스턴스가 생성되고 실행될 때 어드바이스 적용 여부를 확인할 수 있다.
 실행 시점에 일어나는 포인트컷 적용 여부도 결국 프록시가 있어야 실행 시점에 판단할 수 있다.
 그런데, 컨테이너가 프록시를 생성하는 시점은 컨테이너가 만들어질 때이다.
 실행 시점에 판단되도록 하려면, 미리 모든 빈에 프록시를 만들어 놓고 실행시 비교하는 수 밖에 없다.
 문제는 이렇게 모든 스프링 빈에 AOP 프록시를 적용하려고 하면 스프링이 내부에서 사용하는 빈 중에는 final로 지정된 빈들도 있기 때문에 오류가 발생할 수 있다.
 따라서 이러한 표현식은 최대한 프록시 적용 대상을 축소하는 표현식과 함께 사용해야 한다.
 */

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAtWithinTest {
    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child Proxy={}", child.getClass());
        child.childMethod(); //부모, 자식 모두 있는 메서드
        child.parentMethod(); //부모 클래스만 있는 메서드
    }
    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }
        @Bean
        public Child child() {
            return new Child();
        }
        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }
    static class Parent {
        public void parentMethod(){} //부모에만 있는 메서드
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod(){}
    }
    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {

        //@target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        //즉 포인트컷 범위에 부모타입이 포함된다는 건 관련된 상위 Bean
        @Around("execution(* com.minod.aop..*(..)) && @target(com.minod.aop.pointcuts.annotation.ClassAop)")
                public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
                log.info("[@target] {}", joinPoint.getSignature());
                return joinPoint.proceed();
                }

        //@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용 되지 않음
        @Around("execution(* com.minod.aop..*(..)) && @within(com.minod.aop.pointcuts.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
