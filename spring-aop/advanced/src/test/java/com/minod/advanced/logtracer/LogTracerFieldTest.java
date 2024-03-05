package com.minod.advanced.logtracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogTracerFieldTest {
    LogTracerField trace = new LogTracerField();

    @Test
    void begin_end_level2() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello12");
        trace.end(status2);
        trace.end(status1);
        /**
         * logtracer.LogTracerField -- [1cfa8347-] hello1
         * logtracer.LogTracerField -- [1cfa8347-] |-->hello12
         * logtracer.LogTracerField -- [1cfa8347-] |<--hello12 time=0ms
         * logtracer.LogTracerField -- [1cfa8347-] hello1 time=7ms
         */
    }

}