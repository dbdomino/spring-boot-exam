package com.hello.momory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Conditional(MemoryCondition.class) //추가
@ConditionalOnProperty(name = "memory", havingValue = "on") //추가
// 환경 정보가 `memory=on` 이라는 조건에 맞으면 동작하고, 그렇지 않으면 동작하지 않음
// 기존의 MemoryConfig처럼 직접 조건을 구현하여 클래스로 만들어도 되지만, value형식으로 조건을 놓는 방식
// @ConditionalOnXxx 라고 함.
public class MemoryConfigV2 {

//    @Bean
//    public MemoryController memoryController() {
//        return new MemoryController(memoryFinder());
//    }

    @Bean
    public MemoryFinder memoryFinder() {
        System.out.println("MemoryConfig.memoryFinder() 등록중 ");
        return new MemoryFinder();
    }
}
