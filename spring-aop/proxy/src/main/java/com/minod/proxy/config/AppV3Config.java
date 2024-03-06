package com.minod.proxy.config;

import com.minod.proxy.app.v3.OrderControllerV3;
import com.minod.proxy.app.v3.OrderRepositoryV3;
import com.minod.proxy.app.v3.OrderServiceV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV3Config {
    @Bean
    public OrderControllerV3 orderControllerV3() {
        return new OrderControllerV3(orderServiceV3());
    }
    @Bean
    public OrderServiceV3 orderServiceV3() {
        return new OrderServiceV3(orderRepositoryV3());
    }
    @Bean
    public OrderRepositoryV3 orderRepositoryV3() {
        return new OrderRepositoryV3();
    }
}
