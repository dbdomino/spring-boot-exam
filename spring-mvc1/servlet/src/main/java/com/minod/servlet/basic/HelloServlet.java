package com.minod.servlet.basic;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns =  "/hello") // 서블릿에 등록, HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다.
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);

        System.out.println("hello");
        System.out.println("HelloServlet.service");
        System.out.println("req = " + req);
//        System.out.println("req = " + req);
        System.out.println("resp = " + resp);

        String username = req.getParameter("username"); // 빈값이면 null로나온다??
        if (username == null) System.out.println("username is null"); // null로 잡힘
        System.out.println("username : "+ username);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("hello " + username); // 빈값이면 null로 출력하네.. HTTP body에 들어간다.

    }
}
