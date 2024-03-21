package com.minod.aop.internalissue;

import com.minod.aop.internalissue.aspect.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Import({CallLogAspect.class, CallServiceV0.class})
class CallServiceV0Test {
    @Autowired
    CallServiceV0 service;

    @Test
    void external() {
        service.external();
    }

    @Test
    void internal() {
        service.internal();
    }
}