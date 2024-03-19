package com.minod.aop;

import com.minod.aop.order.aop.AspectV1;
import com.minod.aop.order.aop.AspectV6;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class OrderAopConfig {

    @Bean
    public AspectV1 aspectV1() {
        return new AspectV1();
    }
}
