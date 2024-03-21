package com.minod.aop.aopsample.aspect;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
//    int value(); // 인터페이스 반드시 값입력 필요
    int value() default 3; // 인터페이스 기본값설정으로 값 입력 없어도 상관없음.
}
