package com.minod.aop.aopsample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)") // 어노테이션 이름 패키지명 빼고 불러오는 법, 메서드의 파라미터로 넣기
    public Object doRetryBeforeArgs(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable{
        // Throwable 에러나느 Exception 보다 상위 에러, 내부적으로 처리불가능한 상태 메모리부족이나 상대방측 원인
//        Object[] args = joinPoint.getArgs();
        log.info("[AOP doRetry] {} args={} ", joinPoint.getSignature(), retry);

        int maxRetry = retry.value(); // 재시도 횟수, 어노테이션에서 가져옴.
        Exception exceptionHolder = null; // 예외 객체. 예외생기면 담으려고.

        for (int i = 0; i < maxRetry; i++) { // 실패해서 에러객체에 담겼을지라도, 여러번 proceed() 시도 가능함.
            try{
                log.info("[retry] try count={}/{}", i, maxRetry);
                // 재시도할 경우 time sleep 을 줄 수도 있을 것이다.
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }

        }
        /*catch (Throwable e) {  런타임으로 바꿔던져도 됨.
            throw new RuntimeException(e);
        }*/
        // 최종실패시 마지막에 터진 에러로 던짐.
        throw exceptionHolder;
    }

}
