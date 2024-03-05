package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;

/**
 * 필드에 전략을 보관하는 방식
 * 즉 템플릿 메서드 패턴처럼, 실행하는 메서드를 부모에서 준비해주는 게 아니라, 별도 컨텍스트(객체)를 만들어 쓴다.
 * 익명 클래스 -> 별도 컨텍스트(객체)를 만들어 수행한다.
 * 로깅 추적기실행 로직은 별도 컨텍스트(객체)의 execute에 넣는다. (기존 템플릿 메서드 패턴 처럼 부모객체가 가지고 있는게 아니다.)
 *
 * 이 경우 로깅을 수행하기 위해, 별도 컨텍스트(객체)가 반드시 필요하며, call 또한 외부에서 구현된 것을 주입받아야 한다.
 * - 별도 컨택스트(객체)
 * - call 인터페이스, call 구현체
 *
 */
@Slf4j
public class ContextV1 {
    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }
    public void execute() {
        long startTime = System.currentTimeMillis();

        strategy.call(); // 비즈니스 로직 위임

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
    // contextV1 은 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드이다. 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.
    //쉽게 이야기해서 컨텍스트(문맥)는 크게 변하지 않지만, 그 문맥 속에서 strategy 를 통해 일부 전략이 변경된다 생각하면 된다.

}
