package com.minod.proxy.config.v1_proxy;

import com.minod.proxy.app.v1.*;
import com.minod.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import com.minod.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import com.minod.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import com.minod.proxy.logtracer.LogTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {
    // 프록시가 적용되어 주입시키기 위해 구현체로 실제구현체가 아닌 프록시 객체를 먼저 가리킨다.
    // 따라서 return new 에서도 프록시 객체가 나와야된다.
    // 프록시의 필요조건으로 target, LogTracer가 들어가야 한다. (까다롭네 이거)
    @Bean
    public OrderControllerV1 orderController(LogTracer logTracer) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTracer));
        return new OrderControllerInterfaceProxy(controllerImpl, logTracer);
    }

    @Bean
    public OrderServiceV1 orderService(LogTracer logTracer) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTracer));
        return new OrderServiceInterfaceProxy(serviceImpl, logTracer);
    }
    @Bean
    public OrderRepositoryV1 orderRepository(LogTracer logTracer) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTracer);
    }


}
