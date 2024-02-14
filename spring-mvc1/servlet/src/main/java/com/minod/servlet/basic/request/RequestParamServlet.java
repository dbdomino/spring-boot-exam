package com.minod.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20
 *
 * http://localhost:8080/request-param?username=hello0&username=hello&username=hello2&username=hello3&age=20
 *
 * Get으로도 받을 수 있고, Post로도 받을 수 있네.
 * request.getParameter() 는 GET URL query 파라미터형식, POST HTML Form 형식도 둘다 지원한다.
 * postman api 로도 응답할 수 있음.
 */
@WebServlet(name="requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println();
        System.out.println("RequestParamServlet.service");

        System.out.println("전체 파라미터 조회 - start");
        req.getParameterNames().asIterator()
                .forEachRemaining(param -> System.out.println(param + " = "+ req.getParameter(param)));
        System.out.println("전체 파라미터 조회 - end");

        System.out.println("단일 파라미터 조회 - start");
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        System.out.println("username : "+ username); // 중복으로 들어올경우 첫번째 파라미터로 읽어버린다.
        System.out.println("age : "+ age);

        System.out.println("단일 파라미터 조회 - end");

        System.out.println("이름이 같은 복수 파라미터 조회 - start");
        String[] userNames = req.getParameterValues("username");
        for (String name : userNames) {   // 배열 간단한 for문 양식 기억하자.
            System.out.println( "username : " + name);
        } // http://localhost:8080/request-param?username=hello0&username=hello&username=hello2&username=hello3&age=20
        // 중복된 값이 있더라도 value가 중복으로 나오네

    }
}
