package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**  탬플릿 패턴(부모 클래스-execute 구현됨,
 * 전략 패턴 테스트,(인터페이스-Strategy (전략인터페이스) call만 설계, 구현체 (전략 구현체) (call 구현),
 *                 실행용 Context (실행을 위한 공통 로그로직, call이 이안에 들어가기 위해 구현체를 주입받음.)
 *                 실행용 Context - 서비스, 리포지터리 내에 객체로 만들어서 수행되는 것일 수 있다.
 */
@Slf4j
public class ContextV2Test {
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1(); // context 만들 때 strategy를 미리줄 필요가 없다.

        ContextV2 context = new ContextV2(); // 핵심
        context.execute(strategyLogic1); // 실행할 때 주면 된다. 좀더 유연하게...
        context.execute(new StrategyLogic2()); // 실행할 때 마다 계속 생성할 것인가?? 쨋든 이거도 가능.
    }
}
