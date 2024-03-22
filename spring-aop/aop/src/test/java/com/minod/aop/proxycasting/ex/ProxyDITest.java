package com.minod.aop.proxycasting.ex;

import com.minod.aop.pointcuts.MemberService;
import com.minod.aop.pointcuts.MemberServiceImpl;
import com.minod.aop.proxycasting.ex.aspect.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})// JDK 동적 프록시
@Import(ProxyDIAspect.class)
public class ProxyDITest {
    @Autowired
    MemberService memberService; // 본래 타깃은  com.minod.aop.pointcuts.MemberServiceImpl   이건 인터페이스 기반 구현체가 하나일 경우 자동으로 매핑되어 지원됨. 구현체가 2개이상이면 에러남,
    // 프록시 적용되면 memberService class=class jdk.proxy3.$Proxy55 를 주입 받게 된다. (동적 프록시로 만들어진 객체, 부모 인터페이스를 바라보고 만들어짐)
//    @Autowired
//    MemberServiceImpl memberServiceImpl; // 본래 타깃은  com.minod.aop.pointcuts.MemberServiceImpl
    // 부모 인터페이스 기반으로 만들어진 클래스 memberService class=class jdk.proxy3.$Proxy55가 주입되는데, target기반으로 참조가 불가능하다. (인터페이스 또다른 구현체이다보니 본래 구현체를 모르므로 형변환 불가능함)

    // 즉, JDK 동적 프록시로 만들어진다면  MemberService memberService; 는 인터페이스 기반으로 참조되어 사용되는것, MemberServiceImpl memberServiceImpl;는 target 구체 클래스 기반으로 사용되는것.
    // MemberServiceImpl memberServiceImpl; 의존관계 주입에서 에러난다.
    // Bean named 'memberServiceImpl' is expected to be of type 'com.minod.aop.pointcuts.MemberServiceImpl' but was actually of type 'jdk.proxy3.$Proxy55'


    @Test
    void test01() {
        log.info("memberService class={}", memberService.getClass());
//        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());// 실행하면 에러남. spring.aop.proxy-target-class=false
//        memberServiceImpl.hello("hello");
    }
}
