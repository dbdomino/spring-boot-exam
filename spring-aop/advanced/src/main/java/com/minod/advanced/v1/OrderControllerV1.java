package com.minod.advanced.v1;

import com.minod.advanced.logtracer.HelloTraceV1;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace.begin("OrderController.request()") : 로그를 시작할 때 메시지 이름으로
 *  컨트롤러 이름 + 메서드 이름을 주었다. 이렇게 하면 어떤 컨트롤러와 메서드가 호출되었는지 로그로 편리하게 확인할 수 있다.
 *  물론 수작업이다.
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {
    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")  // http://localhost:8080/v1/request?itemId=12323
    public String request(String itemId) { // http://localhost:8080/v1/request?itemId=ex
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()"); // 로깅 시작
            orderService.orderItem(itemId); // 핵심로직
            trace.end(status); // 로깅 종료
            return "ok";
        } catch (Exception e) { // 핵심로직 에러를 던지기 위해 throw 선언해줌.
            trace.exception(status, e);
            throw new RuntimeException(e); //컨트롤러에서 예외를 던져주자. 공통 익셉션에서 처리하도록
        }

    }
}
