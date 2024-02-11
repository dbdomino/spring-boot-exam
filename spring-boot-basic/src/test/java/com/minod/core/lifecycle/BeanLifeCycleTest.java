package com.minod.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class); // 여기서 networkClient() 호출되면서 객체생성.
//        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close(); //스프링 컨테이너를 종료, ConfigurableApplicationContext 필요
    }
    @Configuration
    static class LifeCycleConfig {
        @Bean   // Bean의 옵션말고 메서드에 @PostConstruct @PreDestroy 라고 붙여서 실행도 가능하다.
//        @Bean(initMethod = "init", destroyMethod = "close") // 이렇게하면 커스텀 메소드로 콜백 잡기가능, 메소드이름 자유롭게 적을수있으며 스프링 빈이 스프링 코드에 의존하지않음.
        // 코드 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있음.
        // destroyMethod 옵션은 추론(inferred)라는 기능이 있어 'shutdown' 'close'라는 이름의 메소드가 있으면 자동으로 destroyMethod로 매핑시켜준다.
//        @Bean(initMethod = "init")
//        @Bean(initMethod = "init", destroyMethod = "")   // destroy 기능을 사용안하려면 이렇게 적용시키면 됀다.
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            System.out.println("여긴어디");
            return networkClient;
        }

    }
}
