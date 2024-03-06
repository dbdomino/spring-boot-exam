package com.minod.proxy.app.v1;

//상속받았을 때, 여기다 컨트롤러 어노테이션 쓰는게 아니라, Interface에다가 어노테이션 써야한다.
//@Controller  // X
//@RestController  // X
public class OrderControllerV1Impl implements OrderControllerV1 {
    private final OrderServiceV1 orderService;
    public OrderControllerV1Impl(OrderServiceV1 orderService) {
        this.orderService = orderService;
    }


    @Override
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @Override
    public String noLog() {
        return "ok";
    }
}
