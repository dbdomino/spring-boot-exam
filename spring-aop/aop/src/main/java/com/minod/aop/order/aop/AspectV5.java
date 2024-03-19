package com.minod.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/** AOP 구현 시작
 *  스프링 AOP 5번째 버전, 클래스 단위로 Advice 우선 순위로 적용하기,
 *  이건 클래스로 분리해서 @Order를 적용하는 수 밖에 없다.
 *  내부 클래스로 아래처럼 해도 되고, 별도로 외부에 클래스를 빼서 해도 된다.
 *
 *  이렇게하면, 아래의 txAspect가 우선적으로 적용되도록 지정 가능하다.
 *
 *  test.com.minod.aop.AopTest.java 로 테스트
 */
@Slf4j
public class AspectV5 {

    @Aspect
    @Order(2)
    public static class LogAspect {
        //외부에서 참조 시키는 포인트 컷
        @Around("com.minod.aop.order.aop.Pointcuts.allOrder()") //
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] joinPoint.getSignature() = {}", joinPoint.getSignature()); //joinPoint 메서드 머호출한지 보여줌
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class txAspect {
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

}
