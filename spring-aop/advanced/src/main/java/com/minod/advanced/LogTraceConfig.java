package com.minod.advanced;

import com.minod.advanced.logtracer.LogTracer;
import com.minod.advanced.logtracer.LogTracerThreadLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * logTracer를 Bean으로 등록하고 주입해서 사용하기 위해
 */

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTracer logTracer() {
//        return new LogTracerField(); // 정식 1차, v3컨트롤러 서비스 리포지터리 에 연결
        return new LogTracerThreadLocal(); // 정식 2차, v3컨트롤러 서비스 리포지터리 에 연결
        // 인터페이스를 활용한 변경의 수월함.
    }
}
