package com.minod.proxy.config.v2_dynamicproxy.handler;

import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 핸들러, InvocationHandler 상속받아 구현하는게 핵심.
// LogTraceBasicHandler로 로그트레이서가 필요한 곳에 적용 가능한 프록시 구현체다.
public class LogTraceBasicHandler implements InvocationHandler {
    private final Object target;
    private final LogTracer logTracer;
    public LogTraceBasicHandler(Object target, LogTracer logTracer) {
        this.target = target;
        this.logTracer = logTracer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status=null;
        try {
            String methodToSting = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()"; // 클래스명.메서드명()
            // Bean으로 등록하면서, invoke() 가 한번 수행되었다...
            status = logTracer.begin("LogTraceBasicHandler methodToSting : "+methodToSting+" 메타정보라 이렇게 가져오는게 가능");

            // 메서드 동적 호출하려면? 여긴 JDK 프록시 핸들러의 invoke안이다. target의 메서드를 호출해야함.
            // Message는 target객체의 메타 객체를 넣어줬을 것이다. 아닐 경우 런타임 에러 발생한다. 컴파일 에러로는 못잡음.
            // 믿고, Message가 지원하는 invoke(target객체, 인자)를 써야함.
//            result = target.request(itemId);
            Object result = method.invoke(target, args);// method.invoke(target) 인자 없으면 이렇게도 가능

            logTracer.end(status);

            return result; // 이런식으로 프록시에서 작동 한다.
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
