package com.minod.aop.proxycasting.ex.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class ProxyDIAspect {
    @Before("execution(* com.minod.aop..*.*(..))")
    public void AllTrace(JoinPoint joinPoint){
        log.info("[proxyDIAdvice test] {}", joinPoint.getSignature());
    }
}
