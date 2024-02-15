package com.minod.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {
    // 기본적인 구조는 V3와 같다. 대신에 컨트롤러가 ModelView 를 반환하지 않고, ViewName 만 반환한다.
    // 이번 버전은 인터페이스에 ModelView가 없다. model 객체는 파라미터로 전달되기 때문에 그냥 사용하면 되고, 결과
    //로 뷰의 이름만 반환해주면 된다.
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
