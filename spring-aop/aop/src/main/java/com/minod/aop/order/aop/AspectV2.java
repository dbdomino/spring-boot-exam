package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/** AOP 구현 시작
 *  스프링 AOP 2번째 버전, @Around와 @Pointcut 분리하기.
 * @Pointcut 이 포인트컷
 * @Around는 Advice에 들어가는 포인트 컷 옵션임.
 *          -
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* com.minod.aop.order..*(..))")
    private void allOrder() {}// Pointcut signature 라고 함. 반환형은 반드시 void,


    @Around("allOrder()") // 시그니처로 포인트컷 분리한 것 사용
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] joinPoint.getSignature() = {}", joinPoint.getSignature()); //joinPoint 메서드 머호출한지 보여줌
        return joinPoint.proceed();
    }


}
