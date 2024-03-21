package com.minod.aop.aopsample;

import com.minod.aop.aopsample.aspect.RetryAspect;
import com.minod.aop.aopsample.aspect.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({TraceAspect.class, RetryAspect.class})// Aspect 붙이면 로그 자동으로 붙이기 가능
@SpringBootTest
public class SampleTest {
    @Autowired
    SampleService examService;
    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            examService.request("data" + i);
//            log.info("i = {} , complete", i); // TraceAspect 만들어두면 로그 찍기 TraceAspect 에 구현해둔대로 됨
        }
    }

    @Test
    // 재시도 가능하게 만든 AOP
    void test2() {
        examService.requestRetry("data");
    }
}
