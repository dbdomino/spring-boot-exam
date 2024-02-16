package com.minod.springmvc.controller;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Http메시지 바디의 데이터를 InputStream을 사용해서 직접 읽을 수 있다.
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("oks");
    }


    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        // param으로 request를 InputStream을 사용해서 바로 읽을 수 있다.
//        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

//        response.getWriter().write("oks");
        responseWriter.write("oks");
    }

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        // 스프링 만의 방식으로 대응하자. HttpEntity 외워야한다. HTTP 메시지를 그대로 주고받는 형식이라고 함.
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.put("test","2");   // put을 하려면 List로 넣어야함. ArrayList라던지
        multiValueMap.add("test","2");
        multiValueMap.add("test","3");

        // HttpEntity를 응답에도 사용할 수는 있다. 다만 헤더같은건 MultiValueMap으로 넣어야함.
//        return new HttpEntity<>("messageBody");
        return new HttpEntity<>("messageBody",multiValueMap);
//        return new ResponseEntity<>("messageBody", HttpStatus.CREATED); // 응답관련해서 컨트롤도 가능함.
    }
    /*
스프링 MVC는 다음 파라미터를 지원한다.
    HttpEntity: HTTP header, body 정보를 편리하게 조회
        메시지 바디 정보를 직접 조회
        요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam  X, @ModelAttribute  X
    HttpEntity는 응답에도 사용 가능
        메시지 바디 정보 직접 반환
        헤더 정보 포함 가능
        view 조회X
HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다.
    RequestEntity
        HttpMethod, url 정보가 추가, 요청에서 사용
ResponseEntity
        HTTP 상태 코드 설정 가능, 응답에서 사용
        return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)
     */

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {
        // 단순하게 RequestBody 를 사용하여 응답 바디를 바로 파라미터로 받을수도 있다.. 실무에서 잘쓰임
        // 헤더 정보가 필요하다면  HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
        log.info("messageBody={}", messageBody);

        return "oksss";
    }
    /*
    요청 파라미터 vs HTTP 메시지 바디
    요청 파라미터를 조회하는 기능: @RequestParam  , @ModelAttribute
    HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody

    @ResponseBody
    @ResponseBody 를 사용하면 응답 결과를 return 시키는 것으로, response HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
    물론 이 경우에도 view를 사용하지 않는다.
     */



}
