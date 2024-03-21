package com.minod.aop.internalissue;

import com.minod.aop.internalissue.aspect.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import({CallLogAspect.class, CallServiceV1.class})
class CallServiceV1Test {
    @Autowired
    CallServiceV1 service;

    @Test
    void external() {
        service.external();
    }

    @Test
    void internal() {
        service.internal();
    }
}