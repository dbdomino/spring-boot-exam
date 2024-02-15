package com.minod.servlet.web.springmvc.before;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import java.io.IOException;

@Component("/springmvc/request-handler") // bean 네임으로 찾기
public class MyHttpRequestHandler implements HttpRequestHandler {
    // 핸들러 어뎁터 찾기
    /*
    0 = RequestMappingHandlerAdapter   : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
    1 = HttpRequestHandlerAdapter      : HttpRequestHandler 처리 ->여기서걸림 implements HttpRequestHandler 해줬으니까
이것이 지금 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 맵핑어뎁터다.
    2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리
     */

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
