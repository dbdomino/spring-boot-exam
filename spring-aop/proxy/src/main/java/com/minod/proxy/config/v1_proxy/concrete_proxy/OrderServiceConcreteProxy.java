package com.minod.proxy.config.v1_proxy.concrete_proxy;

import com.minod.proxy.app.v2.OrderServiceV2;
import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;

public class OrderServiceConcreteProxy extends OrderServiceV2 {
    private final OrderServiceV2 target; // 프록시 필수조건,
    private final LogTracer logTracer;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTracer logTracer) {
        super(null); // 중요, OrderServiceV2, 부모의 기능은 쓰지 않는다. 부모의 기능은 상속받은걸로 쓰지 않고, 주입받아 쓴다.
        // 그렇다고 구현안된 부모의 기본생성자 호출되어 오류나게 둘수도 없어 super(null)처리, 자식클래스로 프록시구현은 이렇게 한다고함.
        // 아니면 기본생성자를 OrderServiceV2에 만들어주던지 해야되는데, 그럼 주입받아 쓰는 final 때문에 또 꼬인다.
        this.target = target;
        this.logTracer = logTracer;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status=null;
        try {
            status = logTracer.begin("save() Order Service V2 ");

            target.orderItem(itemId);

            logTracer.end(status);
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
