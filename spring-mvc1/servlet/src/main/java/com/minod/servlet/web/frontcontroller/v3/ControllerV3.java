package com.minod.servlet.web.frontcontroller.v3;

import com.minod.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    // 이 컨트롤러는 서블릿 기술을 전혀 사용하지 않는다. 따라서 구현이 매우 단순해지고, 테스트 코드 작성시 테스트 하기 쉽다.
    // request, response로 테스트를 작성하기 어렵다.
    //HttpServletRequest가 제공하는 파라미터는 프론트 컨트롤러가 paramMap에 담아서 호출해주면 된다.
    //응답 결과로 뷰 이름과 뷰에 전달할 Model 데이터를 포함하는 ModelView 객체를 반환하면 된다

    ModelView process (Map<String, String> paramMap);
}
