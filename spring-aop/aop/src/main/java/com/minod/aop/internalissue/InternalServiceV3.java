package com.minod.aop.internalissue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InternalServiceV3 {
    public void internal() {
        log.info("call internal");
    }
}
