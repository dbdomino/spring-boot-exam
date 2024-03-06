package com.minod.proxy.프록시패턴예시;

import lombok.extern.slf4j.Slf4j;

// 프록시도 실제 객체와 그 모양이 같아야 하기 때문에 Subject 인터페이스를 구현해야 한다. (implements Subject)
// 캐시 기능을 제공하는 프록시 예시
@Slf4j
public class ProxySubjectForCache implements Subject{
    private Subject target; // 클라이언트가 프록시를 호출한다면, 프록시가 최종적으로 실제 객체를 호출해야 하다보니, 요청받은 결과를 다시 subject에게 응답함.
    private String cacheValue;

    public ProxySubjectForCache(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        // 구현한 코드를 보면 cacheValue 에 값이 없으면 실제 객체( target )를 호출해서 값을 구한다.
        // 이걸 위해서, 실제 객체를 호출하기 위해 target을 주입받아 두는 것이다.
        log.info("프록시 Subject 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue; // 실제 객체 호출없이 캐시된 value 반환.
    }
}
