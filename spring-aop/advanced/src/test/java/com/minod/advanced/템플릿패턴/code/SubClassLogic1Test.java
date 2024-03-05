package com.minod.advanced.템플릿패턴.code;

import com.minod.advanced.logtracer.template.code.SubClassLogic1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SubClassLogic1Test {
    @Test
    void Logic1Test() {
        SubClassLogic1 t = new SubClassLogic1();
        t.execute();
    }
}