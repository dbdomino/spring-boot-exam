package com.minod.proxy.app.v3;

import org.springframework.stereotype.Service;

@Service // 컴포넌트 스캔
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;
    public OrderServiceV3(OrderRepositoryV3 orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
