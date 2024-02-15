package com.minod.servlet.web.frontcontroller.v5.adapter;

import com.minod.servlet.web.frontcontroller.ModelView;
import com.minod.servlet.web.frontcontroller.v4.ControllerV4;
import com.minod.servlet.web.frontcontroller.v5.MyhandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyhandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    // FrontController3의 p
    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler; // v3일경우.

        // ctrl + alt + m  -> foreach문 메서드로 만들기
        // 1. request로 paramMap만들기
        Map<String, String> paramMap = createParamMap(request);
        HashMap<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);

        // 2. modelView만들기
        ModelView modelView = new ModelView(viewName);

        return modelView;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()   // request의 값 모두 빼는방법 Iterator 쓰기
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));

        return paramMap;
    }
}
