package com.minod.advanced.v2;

import com.minod.advanced.logtracer.HelloTraceV2;
import com.minod.advanced.logtracer.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace.begin("OrderController.request()") : 로그를 시작할 때 메시지 이름으로
 *  컨트롤러 이름 + 메서드 이름을 주었다. 이렇게 하면 어떤 컨트롤러와 메서드가 호출되었는지 로그로 편리하게 확인할 수 있다.
 *  V2에는 beingSync를 넣음
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {
    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")  // http://localhost:8080/v2/request?itemId=12323
    public String request(String itemId) { // http://localhost:8080/v2/request?itemId=ex
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV2.request()"); // 로깅 시작
            orderService.orderItem(itemId, status.getTraceId()); // 핵심로직 서비스에 로깅을 위한 TraceId 추가(??이건좀?)
            trace.end(status); // 로깅 종료
            return "ok";
        } catch (Exception e) { // 핵심로직 에러를 던지기 위해 throw 선언해줌.
            trace.exception(status, e);
            throw new RuntimeException(e); //컨트롤러에서 예외를 던져주자. 공통 익셉션에서 처리하도록
        }

    }
}
