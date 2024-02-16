package com.minod.springmvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {
    // http://localhost:8080/headers
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie
/*
HttpServletRequest
HttpServletResponse
HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
Locale : Locale 정보를 조회한다.
@RequestHeader MultiValueMap<String, String> headerMap   모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
@RequestHeader("host") String host     특정 HTTP 헤더를 조회한다.
        속성
            필수 값 여부: required
            기본 값 속성: defaultValue
@CookieValue(value = "myCookie", required = false) String cookie    특정 쿠키를 조회한다.
        속성
            필수 값 여부: required
            기본 값: defaultValue
*/
    ) {

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";

        /*
        MultiValueMap  하나의 키에 값을 여러개 받을 수 있다.
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.add("keyA", "value1");
        map.add("keyA", "value2");
        // [value1,value2]
        List<String> values = map.get("keyA");
         */
    }

}
