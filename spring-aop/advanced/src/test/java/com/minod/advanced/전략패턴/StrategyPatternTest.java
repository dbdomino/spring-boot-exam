package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class StrategyPatternTest {

    @Test
    void StrategyPatternTestV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();

        log.info("비즈지스 로직 1");// 비즈니스 로직

        long endTime = System.currentTimeMillis();
        long resultTime = endTime-startTime;

        log.info("resultTime = {}",resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();

        log.info("비즈지스 로직 2");// 비즈니스 로직

        long endTime = System.currentTimeMillis();
        long resultTime = endTime-startTime;

        log.info("resultTime = {}",resultTime);
    }
}
