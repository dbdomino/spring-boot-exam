package com.minod.serverstart.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 서블릿 환경에서 실행하기 위해 맞춰야 하는 서블릿 양식이다. 따라 줘야 한다.
 * http://localhost:8080/test
 * 배포하기 위해 war로 말아서 톰캣에 올려야 한다.
 */
// 어노테이션 방식으로 서블릿 만들기, 간편하다, 경로 하나당 정적매핑
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    // servlet 양식에 맞춰 줘야한다... 그래야 서블릿 환경에서 webapplication 동작이 수행된다.

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("TestServlet.service 실행");
        resp.getWriter().println("resp에 찍기");
    }
}
