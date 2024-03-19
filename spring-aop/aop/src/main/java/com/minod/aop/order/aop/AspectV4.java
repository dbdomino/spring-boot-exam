package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/** AOP 구현 시작
 *  스프링 AOP 4번째 버전, 외부에서 참조 시키는 포인트 컷
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
@Slf4j
@Aspect
public class AspectV4 {

    //외부에서 참조 시키는 포인트 컷
    @Around("com.minod.aop.order.aop.Pointcuts.allOrder()") //
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] joinPoint.getSignature() = {}", joinPoint.getSignature()); //joinPoint 메서드 머호출한지 보여줌
        return joinPoint.proceed();
    }

    @Around("com.minod.aop.order.aop.Pointcuts.orderAndService()") // 포인트컷 2개, 여러개 적용가능.  && (AND), || (OR), ! (NOT) 3가지 조합이 가능하다.
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        // Advice 구현....
        try {
            log.info(" [트랜잭션 시작] {} ", joinPoint.getSignature());

            Object result = joinPoint.proceed();

            log.info(" [트랜잭션 종료, 커밋 {} ", joinPoint.getSignature());

            return result;
        } catch (Exception e) {
            log.info(" [트랜잭션 롤백] {} ", joinPoint.getSignature());
            throw e;
        } finally {
            log.info(" [doTransaction finally] ");
        }

    }




}
