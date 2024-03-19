package com.minod.aop.pointcuts;

import com.minod.aop.pointcuts.annotation.ClassAop;
import com.minod.aop.pointcuts.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {
    @Override
    @MethodAop("메서드AOP헬로우")
    public String hello(String param) {
        return "ok";
    }
    public String internal(String param) {
        return "ok";
    }
}
