package com.minod.advanced.v1;

import com.minod.advanced.logtracer.HelloTraceV1;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId) {
        TraceStatus status = null; //로깅정보 객체
        try { // try catch 쓰는 이유, save 에러시 예외 던지려고. 어디로? 컨트롤러로
            status = trace.begin("OrderServiceV1.orderItem() ");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw new RuntimeException(e);
        }
    }
}
