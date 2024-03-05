package com.minod.advanced.logtracer.template;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.TraceStatus;

/**
 * AbstractTemplate 은 템플릿 메서드 패턴에서 부모 클래스이고, 템플릿 역할을 한다.
 * <T> 제네릭을 사용했다. 반환 타입을 정의한다.
 * 객체를 생성할 때 내부에서 사용할 LogTrace trace 를 전달 받는다.
 * 로그에 출력할 message 를 외부에서 파라미터로 전달받는다.
 * 템플릿 코드 중간에 call() 메서드를 통해서 변하는 부분을 처리한다.
 * abstract T call() 은 변하는 부분을 처리하는 메서드이다. 이 부분은 상속으로 구현해야 한다.
 */

public abstract class LogTracerTemplate<T> {
    private final LogTracer trace; // 한번생성되고 바뀔 일이 없어서 final줌, 다만 생성할때 인스턴스가 주입되던지 만들어지던지 해야함.

    protected LogTracerTemplate(LogTracer trace) {
        this.trace = trace;
    }
    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            T result = call(); // 핵심로직 수행
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
    protected abstract T call();
}
