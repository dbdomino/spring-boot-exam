package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** AOP 구현 시작
 *  스프링 AOP 6번째 버전, Advice를 @Around 로 한번에 구현할 수 있음.
 *  또한 세부적으로 나누어서 필요한 부분을 별도로 구현도 가능함.
 *
 * @Around : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환 값 변환, 예외 변환 등이 가능
 * @Before : 조인 포인트 실행 이전에 실행
 * @AfterReturning : 조인 포인트가 정상 완료후 실행
 * @AfterThrowing : 메서드가 예외를 던지는 경우 실행
 * @After : 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)
 *
 * @Around 에서라면, ProceedingJoinPoint joinPoint이다. 규칙이다.
 * joinPoint.proceed를 반드시 지정해줘야한다.
 *
 * 그런데 Before나 다른것들은 proceed를 선언해주지 않고, 구현할 로직만 딱 적어서 적용할 수 있다.
 * 좀 더 명확한 위치에서 기능을 구분하여 구현하는데 사용된다.
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
//@Component
@Slf4j
@Aspect
public class AspectV6 {
    // 아래 소스를 보면, @Around로 구현한 Advice의 try catch문을 기준으로 나눠서 Advice로 구현을 지원해주는 어노테이션들이 있다.
    @Around("com.minod.aop.order.aop.Pointcuts.orderAndService()") // 포인트컷 2개, 여러개 적용가능.  && (AND), || (OR), ! (NOT) 3가지 조합이 가능하다.
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            //@Before
            log.info(" [트랜잭션 시작] {} ", joinPoint.getSignature());

            Object result = joinPoint.proceed();

            //@AfterReturning
            log.info(" [트랜잭션 종료, 커밋 {} ", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info(" [트랜잭션 롤백] {} ", joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info(" [doTransaction finally] ");
        }

    }
    // @Around가  아닌 세부적으로 구현하려면, 파라미터는 Joinpoint joinpoint로 해줘야 한다.
    // @Around 에서라면, ProceedingJoinPoint joinPoint이다. 규칙이다 외우자.
    @Before("com.minod.aop.order.aop.Pointcuts.orderAndService()")
    public void adviceBefore(JoinPoint joinPoint) {
        log.info("[1.before ] ");
//        log.info("[1.before 메소드 실행 전] {}",joinPoint.getSignature());
    }

    /*@AfterReturning("com.minod.aop.order.aop.Pointcuts.orderAndService()")
    public void adviceAfterReturning(JoinPoint joinPoint) {
        log.info("[2.AfterReturning ] ");
//        log.info("[2.AfterReturning 메소드 실행 후] {}",joinPoint.getSignature());
    }*/
    // 위와 다르게 target 메서드 실행 결과를 출력하고 싶으면 출력 할 수 있음. 다만, return 값으로 뭔가 조작은 하더라도, 변경은 못함.
    // return을 사용할 수는 있지만, return 값을 바꾸진 못한다.
    @AfterReturning(value="com.minod.aop.order.aop.Pointcuts.orderAndService()", returning="result")
    public void adviceAfterReturning2(JoinPoint joinPoint, Object result) {
        log.info("[2.AfterReturning ] , result : {}", result);
    }

    /*@AfterThrowing("com.minod.aop.order.aop.Pointcuts.orderAndService()")
    public void adviceAfterThrowing(JoinPoint joinPoint) {
        log.info("[3.AfterThrowing ] ");
//        log.info("[3.AfterThrowing 메소드 에러시] {}",joinPoint.getSignature());
    }*/
    // 파라미터 이름 ex와 throwing 옵션 "ex" 이름이 일치해야 한다. 규칙이다.
    @AfterThrowing(value="com.minod.aop.order.aop.Pointcuts.orderAndService()", throwing="ex")
    public void adviceAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[3.AfterThrowing ], ex : {} ", ex);
    }

    @After("com.minod.aop.order.aop.Pointcuts.orderAndService()")
    public void adviceAfter(JoinPoint joinPoint) {
        log.info("[4.After ] ");
//        log.info("[4.After 메소드 끝날시(에러든 아니든 상관없이 = finally 부분)] {}",joinPoint.getSignature());
    }
}
