package com.minod.advanced.logtracer.collback;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.extern.slf4j.Slf4j;
// 콜백패턴으로 로그트레이서 리팩토링, V5에 적용

@Slf4j
public class LogTracerContextCallback {
    // 로그트레이서는 주입받아야 함.
    private final LogTracer trace;
    public LogTracerContextCallback(LogTracer trace) {
        this.trace = trace;
    }

    // 전략패턴에선 생성자로 strategy를 주입받아야 하나, 콜백 패턴에선 실행명령에서 주입받으면 됨.
    // 여기서 callback 역할이 중요. 핵심로직
    public <T> T execute(LogTracerCallback<T> callback, String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            T result = callback.call(); // 핵심로직 수행, 제네릭 메서드로, 메서드실행시 제네릭 지정 필요.

            trace.end(status);
            return result; // 이건 반환을 왜하나??
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
