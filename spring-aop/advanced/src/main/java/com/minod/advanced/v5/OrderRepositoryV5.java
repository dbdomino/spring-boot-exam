package com.minod.advanced.v5;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.collback.LogTracerCallback;
import com.minod.advanced.logtracer.collback.LogTracerContextCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV5 {
    private final LogTracer trace;

    public void save(String itemId) {
        LogTracerContextCallback template = new LogTracerContextCallback(trace);
        LogTracerCallback<String> callback = () -> {
            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            customSleep(1000);
            return null;
        };
        template.execute(callback, "OrderRepositoryV5.save()");
    }

    private void customSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
