package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;

/**
 * MessageDecorator 는 Component 인터페이스를 구현한다.
 * 프록시가 호출해야 하는 대상을 component 에 저장한다.
 * operation() 을 호출하면 프록시와 연결된 대상을 호출( component.operation()) 하고, 그 응답 값에
 * ***** 을 더해서 꾸며준 다음 반환한다.
 * 예를 들어서 응답 값이 data 라면 다음과 같다.
 * 꾸미기 전: data
 * 꾸민 후 : *****data*****
 */
@Slf4j
public class MessageDecorator  implements Component{
    private Component component;
    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoResult = "*****" + result + "*****";
        log.info("MessageDecorator 꾸미기 적용 전={}, 적용 후={}", result, decoResult);
        return decoResult;
    }

}
