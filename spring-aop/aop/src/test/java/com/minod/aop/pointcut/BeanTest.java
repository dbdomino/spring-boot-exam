package com.minod.aop.pointcut;

import com.minod.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(BeanTest.BeanAspect.class)
@SpringBootTest
public class BeanTest {
    // 스프링 전용 포인트 컷 지시자, Bean이름으로 패턴에 맞는지 검색한다.

    @Autowired
    OrderService orderService;

    @Test
    void success() {
        orderService.orderItem("itemA");

    }

    @Aspect
    static class BeanAspect {
        // Bean 이름이 orderService 이거나 Repository로 끝나는 거면 해당.로직 적용
        @Around("bean(orderService) || bean(*Repository)")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[bean] 네임해당되서 로그찍기 {}", joinPoint.getSignature());
            return joinPoint.proceed(); // throws Throwable 해줘야 됨.
        }
    }


}
