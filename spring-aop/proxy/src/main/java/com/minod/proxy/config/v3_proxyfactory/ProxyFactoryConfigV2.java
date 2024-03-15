package com.minod.proxy.config.v3_proxyfactory;

import com.minod.proxy.app.v2.OrderControllerV2;
import com.minod.proxy.app.v2.OrderRepositoryV2;
import com.minod.proxy.app.v2.OrderServiceV2;
import com.minod.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import com.minod.proxy.logtracer.LogTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;

@Slf4j
public class ProxyFactoryConfigV2 {
    // config 잡을 땐, repository 부터 service Controller 순으로 Bean등록이 정리가 간단하다.
    @Bean
    public OrderControllerV2 orderControllerV2(LogTracer logTracer)  {
        OrderControllerV2 orderController = new OrderControllerV2(orderServiceV2(logTracer));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTracer)); // getAdvisor()는 아래에 커스텀으로 Advisor 만드는 메서드를 Config에서 만듬.
        OrderControllerV2 proxy = (OrderControllerV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTracer logTracer) {
        OrderServiceV2 orderService = new OrderServiceV2(orderRepositoryV2(logTracer));

        ProxyFactory factory = new ProxyFactory(orderService); // target을 orderService로 잡음
        factory.addAdvisor(getAdvisor(logTracer));
        OrderServiceV2 proxy = (OrderServiceV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderService.getClass()); // 클래스정보 CGLIB 으로 나옴, class com.minod.proxy.app.v2.OrderServiceV2$$SpringCGLIB$$0 로 동적으로 생성된 프록시 인스턴스로 확인된다.. 아래 서비스나 리포지터리들마다 class이름이 동적으로 할당됨.
        return proxy;
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTracer logTracer) {
        OrderRepositoryV2 orderRepository = new OrderRepositoryV2();

        ProxyFactory factory = new ProxyFactory(orderRepository);// Bean 마다 프록시팩토리 구분함 일딴.
        factory.addAdvisor(getAdvisor(logTracer));
//        Object proxy = factory.getProxy(); // 기본 반환은 Object로 됨, 프록시 객체다보니 형변환해서 씀
        OrderRepositoryV2 proxy = (OrderRepositoryV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderRepository.getClass()); // 클래스정보 CGLIB 으로 나오는지 보자.
        return proxy; // 어짜피 pointcut 조건에 안맞으면 프록시가 바라보는 target으로 바로 실행됨. 여기서 target은 new OrderRepositoryV1Impl();
    }


    private Advisor getAdvisor(LogTracer logTracer) {
        // Advisor 반환하려면? Advisor의 조건으로 Pointcut과 Advice 필요
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice   스프링 프록시기반 공통로직 추가를 위해 MethodInterceptor 구현체(advice) 작성
        LogTraceAdvice advice = new LogTraceAdvice(logTracer);// 공통로직을 위해 logTracer 주입필요. 초기 주입은 Config에서 시작이네

        // return advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
