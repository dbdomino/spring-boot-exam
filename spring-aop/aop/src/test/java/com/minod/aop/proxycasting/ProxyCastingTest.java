package com.minod.aop.proxycasting;

import com.minod.aop.pointcuts.MemberService;
import com.minod.aop.pointcuts.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

public class ProxyCastingTest {
    @Test
    void jdkProxy() {
        // 인터페이스 기반 구현체로는 JDK동적프록시 기반 프록시 객체 생성가능
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시로 생성하겠다는 소리.

        // 프록시를 인터페이스 기반 타입캐스팅 성공
        MemberService memberServiceProxy= (MemberService) proxyFactory.getProxy(); // 원랜 Object 타입으로 나옴
        // 프록시를 target 기반 타입캐스팅 실패 ( 컴파일 에러는 안남.)
        // JDK 동적 프록시는 단순히 MemberServiceImpl() 이 위에 target으로 등록되었지만, 이 클래스의 부모 인터페이스를 기반으로 만들어진다.
        // 그러다보니 MemberServiceImpl() 정보는 모르므로, 형변환(캐스팅, 타입캐스팅)이 불가능하다.
        MemberServiceImpl memberServiceImplProxy= (MemberServiceImpl) proxyFactory.getProxy();
    }

    @Test
    void CGLIBProxy() {
        // 인터페이스 기반 구현체로는 JDK동적프록시 기반 프록시 객체 생성가능
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB 동적 프록시로 생성하겠다는 소리.

        // 프록시를 인터페이스 기반 타입캐스팅 성공 (같은 부모를 바라보므로 형변환 가능)
        MemberService memberServiceProxy= (MemberService) proxyFactory.getProxy(); // 원랜 Object 타입으로 나옴
        // 프록시를 target 기반 타입캐스팅 성공  (어짜피 target기반으로 만들어졌으므로 가능)
        MemberServiceImpl memberServiceImplProxy= (MemberServiceImpl) proxyFactory.getProxy();
    }
}
