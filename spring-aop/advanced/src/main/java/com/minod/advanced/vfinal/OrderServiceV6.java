package com.minod.advanced.vfinal;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.collback.LogTracerCallback;
import com.minod.advanced.logtracer.collback.LogTracerContextCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV6 {
    private final OrderRepositoryV6 orderRepository;
    private final LogTracer trace;

    public void orderItem(String itemId) {
        LogTracerContextCallback template = new LogTracerContextCallback(trace);
        LogTracerCallback<String> callback = new LogTracerCallback<>() {
            @Override
            public String call() {
                orderRepository.save(itemId); // 파라미터로 받은거 말고 status.getTraceId(), 즉 새로 만든 status 넣어줘야 레벨이 이어져 출력가능해짐.
                return null;
            }
        };
        template.execute(callback, "OrderServiceV6.orderItem() ");

    }
}
