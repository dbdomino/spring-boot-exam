package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratorComponent implements Component{
    private Component component; //실제 객체 = 구현체 (프록시가 이걸 왜 알아야 하나?)
    // 꾸며주는 추가 소스는 아래처럼 메소드로 구현하고, 그 뒤에 구현체 기능을 또 수행하기 위해서 라고 하는데,
    // 스프링에서는 이런식이 아니다. 아예 프록시 객체에서 실행하며
    public DecoratorComponent(Component component) {
        this.component = component;
    }
    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoResult = "*****" + result + "*****";
        log.info("MessageDecorator 꾸미기 적용 전={}, 적용 후={}", result,
                decoResult);
        return decoResult;
    }
}
