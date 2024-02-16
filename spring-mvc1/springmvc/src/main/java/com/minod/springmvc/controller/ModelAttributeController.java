package com.minod.springmvc.controller;

import com.minod.springmvc.domain.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ModelAttributeController {

    @ResponseBody
//    @RequestMapping("/model-attribute-v1")
     public String modelAttributeV1(
             @RequestParam String username,
             @RequestParam int age) {
        HelloData hello= new HelloData();
        hello.setUsername(username);
        hello.setAge(age);
        log.info("username={}, age={}", hello.getUsername(),hello.getAge());
        return "ok";

     }
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV2(
            @ModelAttribute HelloData helloData) { // 객체의 setter를 호출하여 값을 바인딩 해준다.
        // 값을 검증하는 부분이 필요하다. int age 같은거.
        // 이게 핵심
        log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
        return "ok";

    }

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV3(HelloData helloData) { // 객체의 setter를 호출하여 값을 바인딩 해준다.
        // @ModelAttribute도 생략 가능하다.
        // 값을 검증하는 부분이 필요하다. int age 같은거.
        // 이게 핵심
        log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
        return "ok";

    }
    /*
    @ModelAttribute 는 생략할 수 있다.
그런데 @RequestParam 도 생략할 수 있으니 혼란이 발생할 수 있다.
스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
String , int , Integer 같은 단순 타입 = @RequestParam
나머지 = @ModelAttribute  (argument resolver 로 지정해둔 타입 외)
     */

}
