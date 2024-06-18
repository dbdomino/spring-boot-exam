package com.hello.config;


// 외부 라이브러리의 클래스를 bean으로 등록한 것.
// 자동구성을 활용하여 여기 Configuration을 별도 추가하지 않고도 bean으로 등록되도록 만들어보자.
// 라이브러리 만드려면, 자동구성을 모르면 안되네...
// 비결은 라이브러리 프로젝트에 Config 파일을 자동구성으로 추가해 주는 것이다.
//@Configuration
//@Import(MemoryAutoConfig.class)
public class MemoryConfig {
//    @Bean
//    public MemoryFinder memoryFinder() {
//        return new MemoryFinder();
//    }
//    @Bean
//    public MemoryController memoryController() {
//        return new MemoryController(memoryFinder());
//    }
}