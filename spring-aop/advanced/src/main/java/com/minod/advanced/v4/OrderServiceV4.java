package com.minod.advanced.v4;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.template.LogTracerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {
    private final OrderRepositoryV4 orderRepository;
    private final LogTracer trace;

    public void orderItem(String itemId) {
        LogTracerTemplate<String> template = new LogTracerTemplate<String>(trace) {
            @Override
            protected String call() {
                orderRepository.save(itemId); // 파라미터로 받은거 말고 status.getTraceId(), 즉 새로 만든 status 넣어줘야 레벨이 이어져 출력가능해짐.
                return null;
            }
        };
        template.execute("OrderServiceV4.orderItem() ");

    }
}
