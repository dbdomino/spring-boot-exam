package com.minod.proxy.프록시팩토리;

import com.minod.proxy.cglib.ConcreteService;
import com.minod.proxy.cglib.ServiceImpl;
import com.minod.proxy.cglib.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice()); // TimeAdvice 는 앞서 설명한 MethodInterceptor 인터페이스를 구현한다.
        // 타겟 클래스가 구체 클래스이던, 인터페이스 기반 클래스이던 상관없다는게 aop지원 MethodInterceptor 구현체, Advice구현체를 쓰는것이 핵심
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();
        // ServiceInterface 의 save()대신 프록시 객체의 TimeAdvice()에 있는 invoke()가 대신 실행됨.

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice()); // TimeAdvice 는 앞서 설명한 MethodInterceptor 인터페이스를 구현한다.
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call(); // 이거 호출하면, 프록시 팩토리에 넣어둔 TimeAdvice() 의 invoke()를 꺼내서 쓴다.
        // call() 은 ConcreteService의 메서드이며, call()대신 프록시 객체의 TimeAdvice를 통해 invoke()가 대신 실행됨.
        // Advice라고 부르는 이유는? MethodInterceptor구현체 지만 최상위가 Advice라서.

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("proxyTargetClass 옵션으로 인터페이스가 있어도 CGLIB으로 프록시 객체를 만듬. 즉 클래스 기반 프록시 사용 ")
    void proxyTargetProxy() {
        // target은 인터페이스 기반 구현체인데, CGLIB가 더 좋아! CGLIB쓸래! 할거면
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // target기반, 인터페이스 기반이 아닌 target기반, 구체 클래스 기반 설정을 true로 해준다.
        proxyFactory.addAdvice(new TimeAdvice()); // TimeAdvice 는 앞서 설명한 MethodInterceptor 인터페이스를 구현한다.
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save(); // 이거 호출하면, 프록시 팩토리에 넣어둔 TimeAdvice() 의 invoke()를 꺼내서 쓴다.
        // call() 은 ConcreteService의 메서드이며, call()대신 프록시 객체의 TimeAdvice를 통해 invoke()가 대신 실행됨.
        // Advice라고 부르는 이유는? MethodInterceptor구현체 지만 최상위가 Advice라서.

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
