package com.minod.aop.internalissue.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class CallLogAspect {
    @Before("execution(* com.minod.aop.internalissue..*.*(..)) ")
    public void callLog(JoinPoint joinPoint){
        log.info(" callLog aop={}", joinPoint.getSignature());
    }
    // 모든 메서드에 적용.
}
