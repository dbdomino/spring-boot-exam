package com.minod.proxy.리플랙션동적프록시.jdkdynamic;

import com.minod.proxy.리플랙션동적프록시.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {
    @Test
    void dynamicA() {
        AInterface target = new AImpl(); // 타켓 객체 A, 인터페이스명으로 참조.
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 프록시 객체 생성. (java.lang.reflex)
        // JDK 기준 프록시 객체 생성하는 기술임. 별도로 정리해두고 긁어서 쓰면됨.
        // Proxy.newProxyInstance(어디생성될지 클래스로더지정, 들어가야할 인터페이스 배열(1개이거나 여러개이거나, 배열로), handler(공통로직) ) 3개 넣으면 프록시객체
        // 반환은 Object타입으로 되며, 형변환(어디생성될지 기준) 해줘야 함. 내가 이거 왜하고있냐
        AInterface proxyInstance = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        proxyInstance.call(); // 보면, call()을 했는데, 타임프록시(handler)  의 invoke() 가 실행 됨.
        // 왜그렇냐면, proxyInstance의 call()을 수행하면, call()을 바로 실행하는게 아니라 handler의 invoke()를 대신 수행 하게됨.
        // 대신 수행하는게 핵심임. 프록시의 어떤 메소드를 호출하더라도 handler의 invoke()를 대신 수행한다. 수행한 메서드는 invoke() 에 method의 실행 파라미터로 넘겨주고 실행된다.

        log.info("targetClass={}", target.getClass());
        log.info("proxyInstanceClass={}", proxyInstance.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl(); // 타켓 객체 A, 인터페이스명으로 참조.
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

//        BInterface proxyInstance = (BImpl) Proxy.newProxyInstance(BImpl.class.getClassLoader(), new Class[]{BInterface.class}, handler); // 이건안됨.
        BInterface proxyInstance = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);
        proxyInstance.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyInstanceClass={}", proxyInstance.getClass());
    }


}
