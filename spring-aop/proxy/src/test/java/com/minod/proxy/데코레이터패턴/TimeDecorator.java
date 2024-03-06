package com.minod.proxy.데코레이터패턴;

import lombok.extern.slf4j.Slf4j;
// TimeDecorator 는 실행 시간을 측정하는 부가 기능을 제공한다. 대상을 호출하기 전에 시간을 가지고 있다가
// 대상의 호출이 끝나면 호출 시간을 로그로 남겨준다.
@Slf4j
public class TimeDecorator  implements Component{
    private Component component;
    public TimeDecorator(Component component) {
        this.component = component;
    }
    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = component.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);
        return result;
    }
}
