package com.minod.proxy.logtracer.vfinal;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.collback.LogTracerCallback;
import com.minod.advanced.logtracer.collback.LogTracerContextCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV6 {
    private final LogTracer trace;

    public void save(String itemId) {
        LogTracerContextCallback template = new LogTracerContextCallback(trace);
        LogTracerCallback<String> callback = new LogTracerCallback<>() {
            @Override
            public String call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생");
                }
                customSleep(1000);
                return null;
            }
        };
        template.execute(callback, "OrderRepositoryV6.save()");
    }

    private void customSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
