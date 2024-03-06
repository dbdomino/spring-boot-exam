package com.minod.proxy.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderControllerV2 {
    private final OrderServiceV2 orderService;
    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/v2/request")
    public String request(@RequestParam("itemId") String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
    @RequestMapping("/v2/nolog")
    public String noLog() {
        return "ok";
    }

}
