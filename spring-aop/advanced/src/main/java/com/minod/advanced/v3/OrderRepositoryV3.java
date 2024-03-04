package com.minod.advanced.v3;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {
    private final LogTracer trace;

    public void save(String itemId) {
        TraceStatus status = null;

        try {
            //  Controller에서만 begin, service, repository에선 beginSync로 씀, 이를 위해 TraceId를 파라미터로 받아옴.
            status = trace.begin("OrderRepositoryV3.save()");

            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            customSleep(1000);

            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw new RuntimeException(e);
        }
    }

    private void customSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
