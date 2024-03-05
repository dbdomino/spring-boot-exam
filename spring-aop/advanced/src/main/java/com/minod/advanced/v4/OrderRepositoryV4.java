package com.minod.advanced.v4;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.template.LogTracerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {
    private final LogTracer trace;

    public void save(String itemId) {
        LogTracerTemplate<String> template = new LogTracerTemplate<>(trace){
            @Override
            protected String call() {
                // 저장 로직
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생");
                }
                customSleep(1000);
                return null;
            }
        };
        template.execute("OrderRepositoryV4.save()");
    }

    private void customSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
