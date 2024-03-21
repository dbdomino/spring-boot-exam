package com.minod.aop.internalissue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallServiceV0 {
    // external()을 실행하면서 내부 메서드인 internal()이 실행될 때는 프록시 적용안된 메서드internal()이 직접 실행된다.
    // 스프링 프록시의 한계

    public void external() {
        log.info("call external");
        internal(); //내부 메서드 호출(this.internal())
    }
    public void internal() {
        log.info("call internal");
    }
}
