package com.minod.serverstart.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {

    @Bean
    // 여기  Configuration 에서  Bean으로 등록해줬지만, 서블릿에 등록되지않으면
    // bean으로 등록된 HelloController에 접근할 수 없다.
    public HelloController helloController() {
        return new HelloController();
    }
}
