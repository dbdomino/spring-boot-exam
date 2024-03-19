package com.minod.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    // public으로 열어줘야 함.

    @Pointcut("execution(* com.minod.aop.order..*(..))")
    public void allOrder() {}

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}// Service로 끝나는 클래스 이름의 모든 메서드

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {} //


}
