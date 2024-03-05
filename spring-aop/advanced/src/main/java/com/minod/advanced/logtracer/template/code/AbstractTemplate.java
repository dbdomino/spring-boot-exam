package com.minod.advanced.logtracer.template.code;

import lombok.extern.slf4j.Slf4j;

// 추상클래스 생성, 부모가 될 것이다.

@Slf4j
public abstract class AbstractTemplate {
    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call(); //상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
    protected abstract void call();
}
