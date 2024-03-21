package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false") //JDK 동적 프록시
//@SpringBootTest(properties = "spring.aop.proxy-target-class=true") //CGLIB
/**  application.properties 에 입력하여 설정도 가능하다.   기본값이 CGLIB 로 요즘은 다 되는가봄
 *  spring.aop.proxy-target-class=false  JDK 동적프록시
 *  spring.aop.proxy-target-class=true   CGLIB
 */
public class ThisTargetTest {

    @Autowired
    MemberService memberService; // 인터페이스 기반인데, CGLIB로 만들어진다.
    // 요즘은 스프링부트에서 기본적으로 다 CGLIB로 만들어 버린다고 한다.

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    // this 지시자와 target 지시자로 Aspect를 만들어보자
    @Slf4j
    @Aspect
    static class ThisTargetAspect {
        @Around("this(com.minod.aop.pointcuts.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(com.minod.aop.pointcuts.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //this: 스프링 AOP 프록시 객체 대상
        //JDK 동적 프록시는 인터페이스를 기반으로 생성되므로 구현 클래스를 알 수 없음
        //CGLIB 프록시는 구현 클래스를 기반으로 생성되므로 구현 클래스를 알 수 있음
        @Around("this(com.minod.aop.pointcuts.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
        //target: 실제 target 객체 대상
        @Around("target(com.minod.aop.pointcuts.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

    }


}
