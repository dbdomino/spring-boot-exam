package com.minod.aop;

import com.minod.aop.order.OrderRepository;
import com.minod.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopinfo() {
        // AopUtils.isAopProxy(...) 을 통해서 AOP 프록시가 적용 되었는지 확인할 수 있다.
        // 현재 AOP 관련 코드 작성안해서 프록시가 적용되지 않고, 결과도 false 를 반환해야 정상이다.
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex")) // ex 로 넣을경우 익셉션발생 성공
                .isInstanceOf(IllegalStateException.class);
    }

}
