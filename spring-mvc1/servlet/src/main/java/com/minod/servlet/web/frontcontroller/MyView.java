package com.minod.servlet.web.frontcontroller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render (HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        // dispatcher.forward() : 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이 발생한다
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. model 의 데이터를 request로 전부 전해준다., jsp화면에서 값을 받아 출력하기 위해
        modelToRequestAttribute(model, request);  // ctrl + alt + m  -> foreach문 메서드로 만들기
        // 2. dispatcher 로 viewPath의  jsp경로 열어주기
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private static void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach( (key, value) -> request.setAttribute(key,value));
    }
}
