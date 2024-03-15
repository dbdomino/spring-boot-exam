package com.minod.proxy.config.v2_dynamicproxy;

import com.minod.proxy.app.v1.*;
import com.minod.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import com.minod.proxy.logtracer.LogTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;


// 프록시 구현체, handler를 Bean으로 등록해서 사용.  LogTraceBasicHandler(Object target, LogTracer logTracer)

/**
 * 이전에는 프록시 클래스를 직접 개발했지만, 이제는 JDK 동적 프록시 기술을 사용해서 각각의 Controller ,
 * Service , Repository 에 맞는 동적 프록시를 생성해주면 된다.
 * LogTraceBasicHandler : 동적 프록시를 만들더라도 LogTrace 를 출력하는 로직은 모두 같기 때문에 프록
 * 시는 모두 LogTraceBasicHandler 를 사용한다.(각각 Bean마다 new LogTraceBasicHandler()를 따로 만들어 주는 이유, 타겟이 다 다르기 때문)
 */
@Configuration
public class DynamicProxyBasicConfig {
    // 여기서 로그트레이서 빨간줄 나오는건, 메인에서 등록시켜줌으로 해결가능

    @Bean
    public OrderControllerV1 orderControllerV1(LogTracer logTracer){
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTracer));
//        return new OrderRepositoryV1Impl();  // 이걸 반환하는게 아니라 프록시를 반환해야 한다...
//        Object proxyInstance = Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(), new Class[]{OrderRepositoryV1.class}, new LogTraceBasicHandler(orderController, logTracer));
        OrderControllerV1 proxyInstance = (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(), new Class[]{OrderControllerV1.class}, new LogTraceBasicHandler(orderController, logTracer));
        return proxyInstance;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTracer logTracer) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTracer));
        OrderServiceV1 proxyInstance = (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(), new Class[]{OrderServiceV1.class}, new LogTraceBasicHandler(orderService, logTracer));
        return proxyInstance;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTracer logTracer) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        OrderRepositoryV1 proxyInstance = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(), new Class[]{OrderRepositoryV1.class}, new LogTraceBasicHandler(orderRepository, logTracer));
        return proxyInstance;
    }

}
