package com.minod.proxy.cglib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {
    private final Object target; // 프록시 구현을 위해 대상클래스 target 필요
    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    // public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { 기존이랑 인자가 좀 다르다, MethodProxy 라는게 추가됨(그냥 그렇다고함)
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("TimeMethodInterceptor 실행, 상속받은 MethodInterceptor 핸들러 구현체에서....");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeMethodInterceptor 실행, 걸린시간 = {}, 상속받은 MethodInterceptor 핸들러 구현체에서.... ",resultTime);
        return result;
    }
}
