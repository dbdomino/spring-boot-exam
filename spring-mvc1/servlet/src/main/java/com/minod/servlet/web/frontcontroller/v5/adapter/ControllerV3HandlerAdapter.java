package com.minod.servlet.web.frontcontroller.v5.adapter;

import com.minod.servlet.web.frontcontroller.ModelView;
import com.minod.servlet.web.frontcontroller.v3.ControllerV3;
import com.minod.servlet.web.frontcontroller.v5.MyhandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyhandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3); // true flase 인지 구분하기
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler; // v3일경우.

         // ctrl + alt + m  -> foreach문 메서드로 만들기
        // 1. request로 paramMap만들기
        Map<String, String> paramMap = createParamMap(request);
        // 2. modelView만들기
        ModelView modelView = controller.process(paramMap);

        return modelView;

    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()   // request의 값 모두 빼는방법 Iterator 쓰기
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));

        return paramMap;
    }
}
