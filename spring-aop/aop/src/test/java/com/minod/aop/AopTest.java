package com.minod.aop;

import com.minod.aop.order.OrderRepository;
import com.minod.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class AopTest {

    // 컴포넌트 스캔으로 Bean등록 된 것을 자동 주입.
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopinfo() {
        // AopUtils.isAopProxy(...) 을 통해서 AOP 프록시가 적용 되었는지 확인할 수 있다.
        // Test실행시 SpringBootTest 실행 기준은 Main의 AopAllication(실행파일) 기준이다. 이걸로 Bean등록 된 것도 확인해서 볼 수 있다.
        // 스프링 AOP 적용을 위해 @Aspect에 포함된 클래스가 Bean으로 등록된 것이라면, 프록시를 거쳐 동적 객체로 새로 만들어짐.
        // 동적 객체로 만들어진거라면 true, 아니라면 false
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    @DisplayName("orderService 의 메서드 실행")
    void test1() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex")) // ex 로 넣을경우 익셉션발생 성공
                .isInstanceOf(IllegalStateException.class);
    }

}
