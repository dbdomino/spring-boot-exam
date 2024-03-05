package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;

// call 메서드 구현체 1
@Slf4j
public class StrategyLogic2 implements Strategy{
    @Override
    public void call() {
        log.info("전략패턴2 비즈니스 로직2 실행");
    }
}
