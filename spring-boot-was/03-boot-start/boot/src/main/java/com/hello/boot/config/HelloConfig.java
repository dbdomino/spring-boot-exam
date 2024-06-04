package com.hello.boot.config;

import com.hello.boot.controller.HelloController;
import org.springframework.context.annotation.Bean;

// 컴포넌트 스캔으로 사용할거라 @Configuration 안넣음.
public class HelloConfig {

    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
