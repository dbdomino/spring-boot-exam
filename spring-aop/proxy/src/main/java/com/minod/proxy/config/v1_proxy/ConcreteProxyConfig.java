package com.minod.proxy.config.v1_proxy;

import com.minod.proxy.app.v2.OrderControllerV2;
import com.minod.proxy.app.v2.OrderRepositoryV2;
import com.minod.proxy.app.v2.OrderServiceV2;
import com.minod.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import com.minod.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import com.minod.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import com.minod.proxy.logtracer.LogTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {
    // 프록시가 적용되어 주입시키기 위해 구현체로 실제구현체가 아닌 프록시 객체를 먼저 가리킨다.
    // 따라서 return new 에서도 프록시 객체가 나와야된다.
    // 프록시의 필요조건으로 target, LogTracer가 들어가야 한다. (까다롭네 이거)
    @Bean // Bean이름 = 메서드 이름이다.
    public OrderControllerV2 orderControllerV2(LogTracer logTracer) {
        OrderControllerV2 controllerImpl = new OrderControllerV2(orderServiceV2(logTracer));
        return new OrderControllerConcreteProxy(controllerImpl, logTracer);
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTracer logTracer) {
        OrderServiceV2 serviceImpl = new OrderServiceV2(orderRepositoryV2(logTracer));
        return new OrderServiceConcreteProxy(serviceImpl, logTracer);
    }
    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTracer logTracer) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(repositoryImpl, logTracer);
    }


}
