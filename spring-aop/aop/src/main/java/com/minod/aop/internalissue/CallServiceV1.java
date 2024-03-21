package com.minod.aop.internalissue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class CallServiceV1 {
    private CallServiceV1 service;

    // 이게 setter로 순환참조,
    @Autowired // @Component등록 되어있어야 빨간줄 안뜸. 안그럼 Bean리스트에 자기자신이 등록되지 않은상태라서 완성이안됨.
    public void setCallServiceV1(CallServiceV1 service){
        this.service = service;
    }

    public void external() {
        log.info("call external");
//        internal(); //내부 메서드 호출(this.internal())
        service.internal(); //외부 메서드 호출( 자기 자신 인스턴스를 참조 받는 것이다. 특이하네)
    }
    public void internal() {
        log.info("call internal");
    }
}
