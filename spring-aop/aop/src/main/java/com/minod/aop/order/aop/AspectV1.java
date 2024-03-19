package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/** AOP 구현 시작
 *  스프링 AOP 첫번째 버전, @Aspect 로 Advisor 구현
 *  @Aspect 를 포함한 org.aspectj 패키지 관련 기능은 aspectjweaver.jar 라이브러리가 제공하는 기능이다
 *  스프링이 Aspect-J 프레임워크를 차용해서 구현한 라이브러리다. Aspect-J 프레임워크를 쓰는 건 아니다.
 *  실제 AspectJ가 제공하는 컴파일, 로드타임 위버 등을 사용하는 것은 아니다.
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
//@Component
@Slf4j
@Aspect
public class AspectV1 {
    // Object로 반환이 중요  @Around안의 값은 포인트 컷, doLog 구현소스는 Advice
    @Around("execution(* com.minod.aop.order..*(..))") // 패키지와 그 하위 패키지(..)를 모두포함,
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] joinPoint.getSignature() = {}", joinPoint.getSignature()); //joinPoint 메서드 머호출한지 보여줌
        return joinPoint.proceed();
    }

}
