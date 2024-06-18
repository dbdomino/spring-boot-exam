package com.hello.momory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

//@Configuration
@Conditional(MemoryCondition.class) // 이걸 추가하면 어떤 일이 생기는가? (빈이 등록 안되고 실행도 안된다.)
/** 이제 `MemoryConfig` 의 적용 여부는 `@Conditional` 에 지정한 `MemoryCondition` 의 조건에 따라 달라진다.
 *  MemoryCondition` 의 Override메서드인 `matches()` 를 알아서 실행한다.( implements Condition 이 되게 핵심이네)
 *  그 결과가 `true` 이면 `MemoryConfig` 는 정상 동작한다.
 *  따라서 `memoryController` , `memoryFinder` 가 빈으로 등록된다.
 *
 *  MemoryCondition` 의 실행결과가 `false` 이면 `MemoryConfig` 는 무효화 된다. 그래서
 * `memoryController` , `memoryFinder` 빈은 등록되지 않는다.
 */
public class MemoryConfig {

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
