package com.minod.advanced.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV0 {
    private final OrderServiceV0 orderService;

    @GetMapping("/v0/request")  // http://localhost:8080/v0/request?itemId=12323;
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
}
