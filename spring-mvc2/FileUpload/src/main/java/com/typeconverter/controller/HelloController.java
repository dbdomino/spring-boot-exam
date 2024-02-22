package com.typeconverter.controller;

import com.typeconverter.converter.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // 기본적으로 파라미터 문자로 받음.
        Integer intValue = Integer.valueOf(data);   //타입변경 하는 과정
        log.info("intValue = {}", intValue);
        return "ok";
    }

    // 스프링 MVC제공하는 @RequestParam  (스프링이 중간에서 타입변환해줌.) @ModelAttribute , @PathVariable 마찬가지
    //
    @GetMapping("/hello-v2")
    public String helloConverter(@RequestParam Integer data, HttpServletRequest request) {
        log.info("data instance = {}", data.getClass().getName());
        log.info("data = {}", data);
        return "ok";
    }

    //  만약 개발자가 새로운 타입을 만들어서 변환하고 싶으면 어떻게 하면 될까?
    // 직접 문자 -> 숫자로 변경 구현 하거나, Boolean 타입을 숫자로 변경하는 것도 가능하다

    /**
     * WebConfig 의 addFormatters 에 컨버전서비스를 등록해둿기에 String으로 받은 정보를 ipPort객체에 매핑 가능하다.
     * http://localhost:8080/ip-port?ipPort=127.0.0.2:7894
     */
    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort IP = " + ipPort);
        System.out.println("ipPort Port = " + ipPort);
        return "OK";
        /*
        처리 과정
@RequestParam 은 @RequestParam 을 처리하는 ArgumentResolver 인
RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환한다. 부모 클
래스와 다양한 외부 클래스를 호출하는 등 복잡한 내부 과정을 거치기 때문에 대략 이렇게 처리되는 것으로 이해해도 충
분하다. 만약 더 깊이있게 확인하고 싶으면 IpPortConverter 에 디버그 브레이크 포인트를 걸어서 확인해보자.
         */

    }
}
