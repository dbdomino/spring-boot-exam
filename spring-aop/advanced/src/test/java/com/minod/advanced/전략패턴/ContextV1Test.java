package com.minod.advanced.전략패턴;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**  탬플릿 패턴(부모 클래스-execute 구현됨,
 * 전략 패턴 테스트,(인터페이스-Strategy (전략인터페이스) call만 설계, 구현체 (전략 구현체) (call 구현),
 *                 실행용 Context (실행을 위한 공통 로그로직, call이 이안에 들어가기 위해 구현체를 주입받음.)
 *                 실행용 Context - 서비스, 리포지터리 내에 객체로 만들어서 수행되는 것일 수 있다.
 */
@Slf4j
public class ContextV1Test {
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1); // 핵심
        context1.execute();
        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2); // 핵심
        context2.execute();
    }
    @Test
    void strategyV2() {
        // 익명 클래스로 call을 구현하는 구현체 생성
        Strategy strategyNoname = new Strategy() {
            @Override
            public void call() {
                log.info("전략패턴에 익명 클래스 사용 .");
            }
        };
        ContextV1 context2 = new ContextV1(strategyNoname); // 핵심
        context2.execute();
    }

    // 익명 클래스로 call을 구현하는 구현체 생성, Strategy에 참조 후 넣는게아니라 바로 주입도 가능.
    // 결국 익명클래스로 call을 구현하던지, call구현체를 만들던지, call구현은 필수네.
    // 핵심이 Context 객체 생성에 들어간다. 이해가 안되겠지만,
    // 코드짧게 만들려고 이런거다. V2와 같은 결과 기능임.
    // 인라인으로 넣어버리면서 코드를 줄인 건데, 난 이런게 더 불편하고 싫다. 이미 다 만들고 정리하는거니까 코드해석이 어려워짐.
    @Test
    void strategyV3() {
        ContextV1 context2 = new ContextV1(new Strategy() { //익명함수 내부삽입가능.
            @Override
            public void call() {
                log.info("전략패턴에 익명 클래스 사용 .");
            }
        });
        context2.execute();
    }
    // 익명함수 람다식으로 변경, 깔끔해졌다. new Strategy() 에서 Alt + Enter 누르면 람다식변경 나옴.
    // 조건1, 람다로 변경하려면 인터페이스에 메서드 하나만 있어야 한다.
    @Test
    void strategyV4() {
        //익명함수 내부삽입가능.
        ContextV1 context2 = new ContextV1(() -> log.info("전략패턴에 익명 클래스 사용 ."));
        context2.execute();
    }
}
