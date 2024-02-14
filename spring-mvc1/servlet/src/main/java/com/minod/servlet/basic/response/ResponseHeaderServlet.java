package com.minod.servlet.basic.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //        super.service(req, resp);
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8)); // 한글응답 가능

        //[status-line]
        resp.setStatus(HttpServletResponse.SC_OK); //200
//        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
        //[response-headers]
        resp.setHeader("Content-Type", "text/plain;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("my-header","hello");
        /* Before
        HTTP/1.1 200
Content-Length: 0
Date: Wed, 14 Feb 2024 12:18:17 GMT
Keep-Alive: timeout=60
Connection: keep-alive
           After
           HTTP/1.1 200
Cache-Control: no-cache, no-store, must-revalidate
Pragma: no-cache
my-header: hello
Content-Type: text/plain;charset=utf-8
Content-Length: 4
Date: Wed, 14 Feb 2024 12:19:02 GMT
Keep-Alive: timeout=60
Connection: keep-alive

         */


        //[Header 편의 메서드]
        content(resp);
        cookie(resp);
        redirect(resp);
        //[message body]
        PrintWriter writer = resp.getWriter();
        writer.println("ok"); // Body 영역에 넣기

    }

    // Content 편의 메서드
    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    // 쿠키 편의 메서드
    private void cookie(HttpServletResponse response) {
//        Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600"); // 안먹히는데?
        Cookie cookie = new Cookie("myCookie", "good"); // jakarta
        cookie.setMaxAge(600); //600초
        cookie.setValue("minod=isgood"); // good 을 대체하게 됨.
        response.addCookie(cookie);

        // 별도로 쿠키 추가로 주기 가능
        Cookie cookie2 = new Cookie("myCookie2", "nice"); // jakarta
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie2);
    }

    // redirect 편의 메서드
    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html

        response.setStatus(HttpServletResponse.SC_FOUND); //302
//        response.setStatus(HttpServletResponse.SC_SEE_OTHER); //303  SC_SEE_OTHER 멀로든 지정가능
//        response.setHeader("Location", "/basic/hello-form.html");   // 이 헤더가 응답헤더에 있으면 자동으로 리다이렉트 된다.
        response.sendRedirect("/basic/hello-form.html");  // 바로 위 줄이랑 똑같은 명령이다.
    }



}
