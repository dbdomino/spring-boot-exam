package com.minod.proxy.cglib;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
//        ConcreteService target = new ConcreteService();  // 타깃 객체
        Object target = new Object();

        Enhancer enhancer = new Enhancer(); // CGLIB는 스프링에서 제공하는 Enhancer 인스턴스를 만들어서 실행해야 한다.
        // 규칙이다.
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new TimeMethodInterceptor(target));
        Object proxyInstance = enhancer.create();// 원하는 객체형식이 있다면 형변환해서 참조하면 됌. 프록시 객체 생성

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {} ", proxyInstance.getClass());

//        proxyInstance.call();
// 프록시 객체 실행, call()뿐만 아니라 모든 메서드 실행하면 handler역할을 하는 MethodInterceptor 구현체(TimeMethodInterceptor)가 실행됨.


    }
}
