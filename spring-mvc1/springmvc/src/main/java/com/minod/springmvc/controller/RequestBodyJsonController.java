package com.minod.springmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minod.springmvc.domain.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();
    // Json body를 받기

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // InputStream으로 받아서 객체에 매핑시켜 값을 다룬다.
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        // @RequestBdody로 String으로 받아서 값을 다룬다.
        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        // @RequestBdody로 객체로 받아서 자동으로 매핑된 값을 다룬다.
        // @ModelAttribute 와 비슷한데, @RequestBody를 생략하면 @ModelAttribute가 적용된다. -> 값이 객체에 매핑이 안된다.
        // 이는 HTTP메시지 바디가 아니라 요청 파라미터를 처리하게 되기 때문이다. -> param으로 읽는게 @ModelAttribute
        // 객체로 다룰 경우 생략될 때 프리미티브 타입은 기본값, String은 null값이 들어감
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
    /*
    @RequestBody는 생략 불가능
    @ModelAttribute 에서 학습한 내용을 떠올려보자.

    스프링은 @ModelAttribute , @RequestParam 과 같은 해당 애노테이션을 생략시 다음과 같은 규칙을 적용한다.
        String , int , Integer 같은 단순 타입 = @RequestParam
        나머지 = @ModelAttribute  (argument resolver 로 지정해둔 타입 외)

    따라서 이 경우 HelloData에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어버린다.
        HelloData data  @ModelAttribute HelloData data
        따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.
     */

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) throws IOException {
        // HttpEntity 로 받아 객체를 꺼내와서 자동으로 매핑된 값을 다룬다.
        HelloData helloData = httpEntity.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) throws IOException {
        // 응답도 객체로 보낼 수 있다.
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return helloData;
    }
    /*
    @ResponseBody
응답의 경우에도 @ResponseBody 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다.
물론 이 경우에도 HttpEntity 를 사용해도 된다.
    @RequestBody 요청
JSON 요청 - HTTP 메시지 컨버터 객체
    @ResponseBody 응답
객체  HTTP 메시지 컨버터 - JSON 응답
     */

}
