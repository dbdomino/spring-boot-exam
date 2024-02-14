package com.minod.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minod.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet  extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Content-Type : application/json  // HTTP 응답으로 JSON을 반환할 때는 content-type을 application/json 로 지정해야 한다.
        resp.setContentType("application/json");
//        resp.setContentType("application/json;charset=utf-8 ");   //이렇게 쓰는건 피하자. application/json 기본이 utf-8이라서 그렇단다, 근데 사용은 가능.
        resp.setCharacterEncoding("utf-8");

        HelloData data = new HelloData();
        data.setUsername("해봐한번?");
        data.setAge(20);
        String result = objectMapper.writeValueAsString(data);
//{"username":"kim","age":20}

        resp.getWriter().write(result);

    }
}
