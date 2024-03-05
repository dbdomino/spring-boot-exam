package com.minod.advanced.v4;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.template.LogTracerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * trace.begin("OrderController.request()") : 로그를 시작할 때 메시지 이름으로
 *  컨트롤러 이름 + 메서드 이름을 주었다. 이렇게 하면 어떤 컨트롤러와 메서드가 호출되었는지 로그로 편리하게 확인할 수 있다.
 *  V2에는 beingSync를 넣음
 *  V3에는 HelloTraceV1 -> LogTracer 변경, beingSync 없어짐. 로그추적기 내부에서 Id동기화 진행
 *  V4에는 템플릿 메서드 패턴 도입, 패턴기반 로그트레이서 적용, 익명 클래스로 로그추적기 기능 구현
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    private final OrderServiceV4 orderService;
    private final LogTracer trace;

    @GetMapping("/v4/request")  // http://localhost:8080/v5/request?itemId=12323
    public String request(String itemId) { // http://localhost:8080/v5/request?itemId=ex
        LogTracerTemplate<String> template = new LogTracerTemplate<>(trace) {
            @Override
            protected String call() {
                orderService.orderItem(itemId); // 핵심로직
//                return null; // 반환은 지금은 쓸데가 없다보니 응답의 return으로 쓰기로 함. generic에 void는 못들어가니까. String으로 넣고 씀.
                return "Ok"; // 반환은 지금은 쓸데가 없음.
            }
        };
//        template.execute("OrderControllerV4.request()"); // 이걸 리턴에 넣어버린다. 신박하지만,.,

        return template.execute("OrderControllerV4.request()");
    }
}
