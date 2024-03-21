package com.minod.aop.internalissue;

import com.minod.aop.internalissue.aspect.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import({CallLogAspect.class, CallServiceV2.class})
class CallServiceV2Test {
    @Autowired
    CallServiceV2 service;

    @Test
    void external() {
        service.external();
    }

    @Test
    void internal() {
        service.internal();
    }
}