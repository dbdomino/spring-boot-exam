package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratorPatternClient {
    private Component component; // 클라이언트는 Component를 바라봄(의존), realComponent를 참조하게됨.

    public DecoratorPatternClient(Component component) {
        this.component = component;
    }
    public void execute() { // 클라이언트의 실행.
        String result = component.operation();
        log.info("result={}", result);
    }
}
