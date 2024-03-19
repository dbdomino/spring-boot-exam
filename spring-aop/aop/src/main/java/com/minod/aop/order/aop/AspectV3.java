package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/** AOP 구현 시작
 *  스프링 AOP 3번째 버전, 로그를 출력하는 기능에 추가로 트랜잭션을 적용하는 코드도 추가해보자.
 *  pointcut 하나로 여러 advice문을 적용하게 될 경우 advice의 우선순위를 스프링에서 알아서 찾아 지정해준다.
 *  메서드별(Advisor 기준으로 먼저 적용할 순서를 수동으로 정할 수 없음.)
 *  클래스 단위로 @Aspect 위에 @Order 를 붙여서 클래스단위로 순서정하는 수 밖에 없음.
 *
 *  테스트 돌려서 확인해보면 된다.
 *  여기에서는 로그를 남기는 순서가 [ doLog() -> doTransaction() ] 순서로 적용된 것으로 확인됨.
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
@Slf4j
@Aspect
public class AspectV3 {

    @Pointcut("execution(* com.minod.aop.order..*(..))")
    private void allOrder() {}// Pointcut signature 라고 함. 반환형은 반드시 void,

    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {}// Service로 끝나는 클래스의 모든 메서드


    @Around("allOrder()") // 시그니처로 포인트컷 분리한 것 사용, 파라미터인  ProceedingJoinPoint joinPoint 가 있어야 @Around도 먹히네
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] joinPoint.getSignature() = {}", joinPoint.getSignature()); //joinPoint 메서드 머호출한지 보여줌
        return joinPoint.proceed();
    }

    @Around("allService() && allOrder()") // 포인트컷 2개, 여러개 적용가능.  && (AND), || (OR), ! (NOT) 3가지 조합이 가능하다.
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
