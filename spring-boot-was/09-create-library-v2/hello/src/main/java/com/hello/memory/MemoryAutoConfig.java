package com.hello.memory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

// 추가1 자동구성 파일 추가
// 추가2 자동구성 대상 파일을 위해 META-INF 폴더와 파일을 직접 추가해 줘야한다.(resource에 수동으로 만들어 추가해주자.)
// resources/META-INF/spring   폴더 추가
// 폴더에 org.springframework.boot.autoconfigure.AutoConfiguration.imports  파일 추가하고 설정파일 추가해주기
// 그래야 스프링부트가 라이브러리 읽으면서 자동으로 추가해준다.
@AutoConfiguration
@ConditionalOnProperty(name="memory", havingValue="on")
public class MemoryAutoConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());

    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
