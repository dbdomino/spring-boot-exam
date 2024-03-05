package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;

// call 메서드 구현체 1
@Slf4j
public class StrategyLogic1 implements Strategy{
    @Override
    public void call() {
        log.info("전략패턴1 비즈니스 로직1 실행");
    }
}
