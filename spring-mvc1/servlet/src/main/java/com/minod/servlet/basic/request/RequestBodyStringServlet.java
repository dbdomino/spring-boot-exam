package com.minod.servlet.basic.request;


import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name="requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
        ServletInputStream inputStream = req.getInputStream();  // byte로 받아서 inputStream에 저장
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // StreamUtils 기억하자.

        System.out.println("Message:body = "+messageBody);

        resp.getWriter().write(messageBody);
    }
}
