package com.minod.aop.internalissue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {
    private final InternalServiceV3 internalServiceV3;

    public CallServiceV3(InternalServiceV3 internalServiceV3) {
        this.internalServiceV3 = internalServiceV3;
    }

    public void external() {
        log.info("call external");
//        internal(); //내부 메서드 호출(this.internal())
//        service.internal(); //외부 메서드 호출( 자기 자신 인스턴스를 참조 받는 것이다. 특이하네)
        internalServiceV3.internal(); // 외부에서 주입받은 메서드.
    }
    // 별도 클래스로 분리
//    public void internal() {
//        log.info("call internal");
//    }
}
