package com.minod.proxy.logtracer.vfinal;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.collback.LogTracerCallback;
import com.minod.advanced.logtracer.collback.LogTracerContextCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace.begin("OrderController.request()") : 로그를 시작할 때 메시지 이름으로
 *  컨트롤러 이름 + 메서드 이름을 주었다. 이렇게 하면 어떤 컨트롤러와 메서드가 호출되었는지 로그로 편리하게 확인할 수 있다.
 *  V2에는 beingSync를 넣음
 *  V3에는 HelloTraceV1 -> LogTracer 변경, beingSync 없어짐. 로그추적기 내부에서 Id동기화 진행
 *  V4에는 템플릿 메서드 패턴 도입, 패턴기반 로그트레이서 적용, 익명 클래스로 로그추적기 기능 구현
 *  V5에는 템플릿 메서드 패턴으로 작성된 로그 트레이서 실행기를, 콜백 패턴으로 작성된 로그 트레이서 실행기로 변경
 *  V6에는 템플릿 메서드 패턴기반 로그 트레이서 실행기를 생성자를 통해 생성하여 모든 요청에서 사용가능하도록 변경
 */
@RestController
//@RequiredArgsConstructor
public class OrderControllerV6 {
    private final OrderServiceV6 orderService;
    private final LogTracerContextCallback template;
//    private final LogTracer trace; // 로그 추적기 실행기로 실행함. 이제 직접 begin 으로 실행하지 않음.

    // 로그추적기를 주입하기 위해 어쩔 수 없이...
    public OrderControllerV6(OrderServiceV6 orderService, LogTracer trace){
        this.orderService = orderService;
//        this.trace = trace;
        this.template = new LogTracerContextCallback(trace); // 로그추적기를 주입
    }


    @GetMapping("/v6/request")  // http://localhost:8080/v6/request?itemId=12323
    public String request(String itemId) { // http://localhost:8080/v6/request?itemId=ex
        String result = template.execute(new LogTracerCallback<String>() {
            @Override
            public String call() {
                orderService.orderItem(itemId); // 핵심로직
                return "반환결과...."; // 딱히 반환할거라곤?
            }
        },"OrderControllerV6.request()");

        return result;
    }
}
