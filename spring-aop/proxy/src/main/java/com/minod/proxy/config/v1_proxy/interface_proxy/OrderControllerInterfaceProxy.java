package com.minod.proxy.config.v1_proxy.interface_proxy;


import com.minod.proxy.app.v1.OrderControllerV1;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;

/**
 * 프록시를 만들기 위해 인터페이스를 구현하고 구현한 메서드에 LogTracer 를 사용하는 로직을 추가한다.
 * 지금까지는 OrderControllerImpl 에 이런 로직을 모두 추가해야했다.
 * 프록시를 사용하면 아래 로직들을 자동으로 프록시가 만들고 처리해준다.
 * 따라서 OrderControllerImpl 코드를 변경하지 않아도 된다.
 */
@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {
    private final OrderControllerV1 target;
    private final LogTracer logTracer;

    @Override
    public String request(String itemId) {
        TraceStatus status=null;
        String result = null;
        try {
            status = logTracer.begin("save() Order Controller V1 ");

            result = target.request(itemId);

            logTracer.end(status);
            return result; // 이런식으로 프록시에서 작동 한다.
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }

    }

    @Override
    public String noLog() {
        return target.noLog(); // noLog() 메서드는 로그를 남기지 않아야 한다. 따라서 별도의 로직 없이 단순히 target 을 호출하면 된다
    }
}
