package com.minod.advanced.logtracer.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic2 extends AbstractTemplate {
    @Override
    protected void call() {
        log.info("SubClassLogic2 비즈니스 로직2 실행");
    }
}
