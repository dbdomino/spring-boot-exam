package com.minod.advanced.v5;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.collback.LogTracerCallback;
import com.minod.advanced.logtracer.collback.LogTracerContextCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace.begin("OrderController.request()") : 로그를 시작할 때 메시지 이름으로
 *  컨트롤러 이름 + 메서드 이름을 주었다. 이렇게 하면 어떤 컨트롤러와 메서드가 호출되었는지 로그로 편리하게 확인할 수 있다.
 *  V2에는 beingSync를 넣음
 *  V3에는 HelloTraceV1 -> LogTracer 변경, beingSync 없어짐. 로그추적기 내부에서 Id동기화 진행
 *  V4에는 템플릿 메서드 패턴 도입, 패턴기반 로그트레이서 적용, 익명 클래스로 로그추적기 기능 구현
 *  V5에는 템플릿 메서드 패턴으로 작성된 로그 트레이서 실행기를, 콜백 패턴으로 작성된 로그 트레이서 실행기로 변경
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV5 {
    private final OrderServiceV5 orderService;
    private final LogTracer trace;

    @GetMapping("/v5/request")  // http://localhost:8080/v5/request?itemId=12323
    public String request(String itemId) { // http://localhost:8080/v5/request?itemId=ex
        LogTracerContextCallback template = new LogTracerContextCallback(trace);
        String result = template.execute(new LogTracerCallback<String>() {
            @Override
            public String call() {
                orderService.orderItem(itemId); // 핵심로직
                return "반환결과...."; // 딱히 반환할거라곤?
            }
        },"OrderControllerV5.request()");

        return result;
    }
}
