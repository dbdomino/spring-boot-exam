package com.minod.proxy.config.v6_aop;

import com.minod.proxy.config.AppV1Config;
import com.minod.proxy.config.AppV2Config;
import com.minod.proxy.config.v6_aop.aspect.LogTracerAspect;
import com.minod.proxy.logtracer.LogTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTracerAspect logTraceAspect(LogTracer logTracer) {
        return new LogTracerAspect(logTracer);
    }
}
