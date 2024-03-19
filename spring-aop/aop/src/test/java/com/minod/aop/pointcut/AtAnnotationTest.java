package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberService;
import com.minod.aop.pointcuts.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(AtAnnotationTest.AtAnnotationAspect.class) //테스트에서 Import로 빈 등록도 이렇게 한다...
@SpringBootTest
public class AtAnnotationTest {
    // @annotation : 메서드가 주어진 애노테이션을 가지고 있는 메서드라면, 조인 포인트로 매칭
    // 즉, 메서드에 붙은 애너테이션을 가진 것이라면 프록시를 만들어주는 것.
    // 클래스에 붙은게 아니라, 메서드에 붙은 에너테이션을 조인포인트로서 검증할 때 쓴다.
    // @Target @Within 이 클래스에 붙은 애노테이션을 검증하는 조인포인트로 판단하는 포인트 컷 지시자.

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("test hello");
    }


    @Slf4j
    @Aspect
    static class AtAnnotationAspect {
        @Around("@annotation(com.minod.aop.pointcuts.annotation.MethodAop)")
        public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@annotation] 지시자 {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

}
