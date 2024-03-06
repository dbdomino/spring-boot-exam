package com.minod.proxy.프록시패턴예시.concreteproxy;

import com.minod.proxy.프록시패턴예시.concreteproxy.code.ConcreteClient;
import com.minod.proxy.프록시패턴예시.concreteproxy.code.ConcreteLogic;
import com.minod.proxy.프록시패턴예시.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

// 구체형 클래스가 프록시 적용된다면?? 예제
public class ConcreteProxyTest {
    @Test
    void noProxy() { // 프록시 없을 때ㅔ
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    @Test
    void doProxy1() { // client -> timeProxy -> concreteLogic
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteLogic timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();
    }

}
