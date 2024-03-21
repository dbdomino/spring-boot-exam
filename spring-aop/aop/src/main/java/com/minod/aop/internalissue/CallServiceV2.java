package com.minod.aop.internalissue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ObjectProvider(Provider), ApplicationContext를 사용해서 지연(LAZY) 조회
 */
@Slf4j
//@Component
public class CallServiceV2 {
//    private CallServiceV2 service;
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    // 지연 조회를 통해 생성자 주입을 먼저 Provider로 수행
    @Autowired
    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");
//        internal(); //내부 메서드 호출(this.internal())
//        service.internal(); //외부 메서드 호출( 자기 자신 인스턴스를 참조 받는 것이다. 특이하네)
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();
        callServiceV2.internal(); // 외부에서 주입받은 메서드 호출 (Provider로 받아와서 주입)
    }
    public void internal() {
        log.info("call internal");
    }
}
