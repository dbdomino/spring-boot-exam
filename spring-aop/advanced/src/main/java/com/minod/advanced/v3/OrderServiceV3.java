package com.minod.advanced.v3;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;
    private final LogTracer trace;

    public void orderItem(String itemId) {
        TraceStatus status = null; //로깅정보 객체
        try { // Controller에서만 begin, service, repository에선 beginSync로 씀, 이를 위해 TraceId를 파라미터로 받아옴.
            status = trace.begin("OrderServiceV3.orderItem() ");
            orderRepository.save(itemId); // 파라미터로 받은거 말고 status.getTraceId(), 즉 새로 만든 status 넣어줘야 레벨이 이어져 출력가능해짐.
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw new RuntimeException(e);
        }
    }
}
