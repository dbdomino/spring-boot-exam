package com.minod.proxy.프록시패턴예시;

import org.junit.jupiter.api.Test;

public class ProxyPatternTest {
    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }
    @Test
    void cacheProxyTest() {
        Subject realSubject = new RealSubject();
        ProxySubjectForCache cacheProxy = new ProxySubjectForCache(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy); // 클라이언트가 프록시를 먼저 바라봄.
        // 실제 객체 호출은 1초가 필요, 캐시 값 호출은 바로 됨.
        // 캐시(프록시) 이용하면 이런 이점이 있다.
        client.execute();
        client.execute();
        client.execute();
    }
}
