package com.minod.proxy.config.v1_proxy.interface_proxy;


import com.minod.proxy.app.v1.OrderServiceV1;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;

/**
 * 프록시를 만들기 위해 인터페이스를 구현하고 구현한 메서드에 LogTracer 를 사용하는 로직을 추가한다.
 * 지금까지는 OrderServiceImpl 에 이런 로직을 모두 추가해야했다.
 * 프록시를 사용하면 아래 로직들을 자동으로 프록시가 만들고 처리해준다.
 * 따라서 OrderServiceImpl 코드를 변경하지 않아도 된다.
 */
@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1 {
    private final OrderServiceV1 target;
    private final LogTracer logTracer;

    @Override
    public void orderItem(String itemId) { // 맞춰서 구현
        TraceStatus status=null;
        try {
            status = logTracer.begin("save() Order Service V1 ");

            target.orderItem(itemId);

            logTracer.end(status);
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }


    }
}
