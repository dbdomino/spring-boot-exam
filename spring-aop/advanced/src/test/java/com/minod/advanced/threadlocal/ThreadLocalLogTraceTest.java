package com.minod.advanced.threadlocal;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.LogTracerThreadLocal;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

// advanced.logtracer.LogTracerThreadLocal 을 불러와서 테스트 진행
// 쓰레드 로컬이 적용된 상태에서 로그 트레이스가 잘 동작하는지 확인
@Slf4j
public class ThreadLocalLogTraceTest {
    private final LogTracer logTracer = new LogTracerThreadLocal();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = logTracer.begin("stat1");
        TraceStatus status2 = logTracer.begin("stat2");
        logTracer.end(status2);
        logTracer.end(status1);
    }

    @Test
    void begin_exception_level2() {
        TraceStatus status1 = logTracer.begin("안경1");
        TraceStatus status2 = logTracer.begin("안경2");
        logTracer.exception(status2, new IllegalStateException());
        logTracer.exception(status1, new IllegalAccessException());
    }

}
