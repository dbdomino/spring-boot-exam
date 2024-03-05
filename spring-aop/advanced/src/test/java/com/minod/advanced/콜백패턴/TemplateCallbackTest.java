package com.minod.advanced.콜백패턴;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {
    @Test
    void callbackTest() {
        TemplateCallBack template = new TemplateCallBack();
        CallBackLogTracer tracer = new CallBackLogTracer() {
            @Override
            public void call() {
                log.info("CallBackLogTracer 구현체 tracer 익명로직 실행");
            }
        };
//        template.execute(콜백 객체);
        template.execute(tracer); // 이렇게 실행하는게 템플릿 콜백 패턴, JDBC 템플릿도 이런식이라고함.
    }
    @Test // 이런식으로도 가능함. 어렵지만 코드수 줄인 방식 위의 테스트와 같음. 핵심 로직인 구현부를
        // 인터페이스 기반 익명함수로 만들어 구현한 게 핵심.
    void callbackTest2() {
        TemplateCallBack template = new TemplateCallBack();
        template.execute(new CallBackLogTracer() {
            @Override
            public void call() {
                log.info("CallBackLogTracer 구현체 tracer 익명로직2 실행");
            }
        });
    }
}
