package com.minod.advanced.템플릿패턴.code;

import com.minod.advanced.logtracer.template.code.AbstractTemplate;
import com.minod.advanced.logtracer.template.code.SubClassLogic1;
import com.minod.advanced.logtracer.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class templateMethodV1 {
    @Test
    void templateTest1() {
        AbstractTemplate t1 = new SubClassLogic1();
        t1.execute();

        AbstractTemplate t2 = new SubClassLogic2();
        t2.execute();
    }

    /**
     * 템플릿 메서드 패턴, 익명 내부 클래스 사용
     */
    @Test
    void templateMethodTestV2() {
        SubClassLogic1 template0 = new SubClassLogic1() {};
        AbstractTemplate template1 = new AbstractTemplate() { // 익명 클래스로 메서드에다 클래스를 만들어 쓰는 것이다.
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("클래스 이름1={}", template1.getClass());
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("클래스 이름2={}", template2.getClass());
        template2.execute();
    }

}