package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략 패턴, 이름을 누가지었는지는 모르겠으나, V1은 참조를 뺀다면서 Context에서 하는게 많아졌다.
 * 또한 context쓰기위해 strategy의 call을 미리 주입받아야 한다....
 *
 * context 에서 미리 strategy를 주입 받지 않고, execute(Strategy strategy)를 통해 실행할 때
 * call을 주입받아서 사용하자. (그나마 연관 관계를 좀 더 덜었다.)
 */
@Slf4j
public class ContextV2 {
/*  생성자 주입 받던걸 빼버리자.
    private Strategy strategy; //
    public ContextV2(Strategy strategy) {
        this.strategy = strategy;
    }
 */
    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();

        strategy.call(); // 비즈니스 로직 위임

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

}
