package com.minod.serverstart.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping(value="/hello-spring", produces = "text/plain; charset=UTF-8")
    public String hello() {
        System.out.println("HelloController 입니다.");
        return "hello-spring 호출한 hellocontroller 짓입니다.";
    }
}
