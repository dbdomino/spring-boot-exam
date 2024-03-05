package com.minod.advanced.logtracer;

// 이런식으로 인터페이스를 만들어 구현체로 만든 것은, 수동으로 Config에 스프링Bean으로 등록해서 관리하는 게 나을 수 있다.
public interface LogTracer {
    /// 최소한 기능 3가지
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);

}
