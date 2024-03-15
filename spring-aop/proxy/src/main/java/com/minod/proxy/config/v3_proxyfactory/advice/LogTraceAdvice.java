package com.minod.proxy.config.v3_proxyfactory.advice;

import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LogTraceAdvice implements MethodInterceptor {
//    private final Object target; // invoke로 받는 invocation에 target정보 있음.
    private final LogTracer logTracer;
    public LogTraceAdvice(LogTracer logTracer) {
//        this.target = target;
        this.logTracer = logTracer;
    }

    // 리플랙트 기능으로 프록시 구현체인 handler와 다르지만 같은 이름인 invoke()를 Override 가능
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // invocation 안에, 프록시 객체말고 기존 호출된 메소드, 클래스 정보들 다 들어있음.
        TraceStatus status=null;
        try {
            String methodToSting = invocation.getClass() + "." + invocation.getMethod() + "()"; // 클래스명.메서드명()
            status = logTracer.begin("LogTraceBasicHandler methodToSting : "+methodToSting+" 메타정보라 이렇게 가져오는게 가능");

            // 핵심로직
//            Object result = method.invoke(target, args);// method.invoke(target) 인자 없으면 이렇게도 가능
            Object result= invocation.proceed();// invocation 안에 target 정보와 인자들도 다들어가있음.

            logTracer.end(status);
            return result; // 이런식으로 프록시에서 작동 한다.
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }

    }
}
