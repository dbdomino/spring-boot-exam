package com.minod.proxy.config.v1_proxy.concrete_proxy;

import com.minod.proxy.app.v2.OrderControllerV2;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;


public class OrderControllerConcreteProxy extends OrderControllerV2 {
    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTracer logTracer) {
        super(null);
        this.target = target;
        this.logTracer = logTracer;
    }

    private final OrderControllerV2 target;
    private final LogTracer logTracer;

    @Override
    public String request(String itemId) {
        TraceStatus status=null;
        String result = null;
        try {
            status = logTracer.begin("save() Order Controller V2 ");

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
        return target.noLog();
    }
}
