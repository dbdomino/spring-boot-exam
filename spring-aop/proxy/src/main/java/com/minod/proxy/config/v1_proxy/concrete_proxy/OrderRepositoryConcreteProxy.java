package com.minod.proxy.config.v1_proxy.concrete_proxy;

import com.minod.proxy.app.v2.OrderRepositoryV2;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;

public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {
    private final OrderRepositoryV2 target;
    private final LogTracer logTracer;

    public OrderRepositoryConcreteProxy(OrderRepositoryV2 target, LogTracer logTracer) {
        this.target = target;
        this.logTracer = logTracer;
    }

    @Override
    public void save(String itemId) {
        TraceStatus status=null;
        try {
            status = logTracer.begin("save() Order Repository V2 ");

            target.save(itemId);

            logTracer.end(status);
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
