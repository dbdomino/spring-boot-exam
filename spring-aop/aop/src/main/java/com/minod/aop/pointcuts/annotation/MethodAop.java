package com.minod.aop.pointcuts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)// 메서드에 붙이는거면 엘리먼트 타입이 메서드
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAop { // 어노테이션 만들려면 @ 붙여서
    String value(); // 어노테이션이 값을 가질 수 있음.
}
