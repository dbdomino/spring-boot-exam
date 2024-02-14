package com.minod.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minod.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * http://localhost:8080/request-body-json
 *
 * JSON 형식 전송
 * content-type: application/json
 * message body: {"username": "hello", "age": 20}
 *
 */

@WebServlet(name="requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper(); // 머냐이건?

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream servletInputStream = req.getInputStream();

        String message = StreamUtils.copyToString(servletInputStream, StandardCharsets.UTF_8);
        System.out.println("message : "+ message);

        HelloData helloData = objectMapper.readValue(message, HelloData.class); // json 형식 메시지를 객체형식으로 바로 매핑시키는게 가능.

//        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8)); // 한글지원하도록 응답 설정
        resp.setHeader("Content-Type", "text/plain;charset=utf-8");
        resp.getWriter().write(message);
    }
}
