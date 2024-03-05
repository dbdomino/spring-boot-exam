package com.minod.advanced.logtracer;

import org.junit.jupiter.api.Test;

class HelloTraceV2Test {
    @Test
    void begin_end_v2() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("hello2");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), status1.getMessage());// 다음레벨로 이어서 로깅위해 beginSync로 TraceStatus 새로 생성(ID는 같음, 이어진걸로 보이게 하려고.)
        // 선언도 각각, 종료도 각각
        trace.end(status2);
        trace.end(status1);
    }
    @Test
    void begin_exception_v2() {
        HelloTraceV2 trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("hello2");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), status1.getMessage());
        trace.exception(status2, new IllegalStateException());// 에러를 내는게 아니라, 에러로그를 찍는 것.
        trace.exception(status1, new IllegalStateException());
    }

}