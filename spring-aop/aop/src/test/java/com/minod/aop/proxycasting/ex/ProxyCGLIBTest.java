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
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})// JDK 동적 프록시
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})// CGLIB 동적 프록시, 기본값이다.
@SpringBootTest// CGLIB 동적 프록시, 기본값이다.
@Import(ProxyDIAspect.class)
public class ProxyCGLIBTest {
    @Autowired
    MemberService memberService; // 본래 타깃은  com.minod.aop.pointcuts.MemberServiceImpl   이건 인터페이스 기반 구현체가 하나일 경우 자동으로 매핑되어 지원됨. 구현체가 2개이상이면 에러남,
    // 프록시 적용되면 memberService class=class com.minod.aop.pointcuts.MemberServiceImpl$$SpringCGLIB$$0를 주입 받게 된다. (동적 프록시로 만들어진 객체, 부모 인터페이스를 바라보고 만들어짐)
    @Autowired
    MemberServiceImpl memberServiceImpl; // 본래 타깃은  com.minod.aop.pointcuts.MemberServiceImpl


    @Test
    void test01() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
