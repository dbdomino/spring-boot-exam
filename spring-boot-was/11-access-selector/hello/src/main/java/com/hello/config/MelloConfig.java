package com.hello.config;

import com.hello.controller.MelloController;
import com.hello.domain.MelloServe;
import org.springframework.context.annotation.Bean;

public class MelloConfig {
    @Bean
    public MelloController melloController() {
        return new MelloController();
    }

    @Bean
    public MelloServe melloServe() {
        return new MelloServe();
    }
}
