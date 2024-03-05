package com.minod.advanced.logtracer.collback;

// 콜백 역할 담당하는, 것 전략 패턴에서 전략 이었 것
public interface LogTracerCallback<T>{
    T call();
}
