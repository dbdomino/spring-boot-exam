package com.minod.aop.aopsample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TraceAspect {

    // Trace 라는 애노테이션이 있는 거라면 프록시 설정하겠다는 소리.
//    @Before("@annotation(com.minod.aop.aopsample.aspect.Trace)")
    public void doTrace(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        log.info("[AOP doTrace] {} args={} ", joinPoint.getSignature(), args);

    }
    @AfterReturning("@annotation(com.minod.aop.aopsample.aspect.Trace)")
    public void doTraceAfterReturning(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        log.info("[AOP doTrace] {} args={} ", joinPoint.getSignature(), args);

    }

}
