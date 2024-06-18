package hello.helloenv.config;

import hello.helloenv.datasource.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.util.List;

// MyDataSource 를 스프링 빈으로 등록하는 자바 설정이다.

/**
 * 스프링의 Environment를 활용해서 변수로 등록하기
 * 이 방식의 단점은 Environment 를 직접 주입받고, env.getProperty(key) 를 통해서 값을 꺼내는 과정을 반복 해야 한다는 점이라고 한다.
 * 값 하나 꺼내려면 env.getProperty(key, Type) 를 반복적으로 호출 해줘야 하며, 그러기 위해 Environment에 의존적이다.
 * 스프링은 @Value 를 통해서 외부 설정값을 주입 받는 더욱 편리한 기능을 제공한다.
 */
@Slf4j
@Configuration
public class MyDataSourceEnvConfig {
    private final Environment env;

    public MyDataSourceEnvConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MyDataSource myDataSource() {
        // Environment 인터페이스로 자동 설정된 프로퍼티를 가져올 수 있다.
        // Environment.getProperty(key, Type) 를 호출할 때 타입 정보를 주면 해당 타입으로 변환해준다. (스프링 내부 변환기가 작동한다.)
        // 기본적으로 String타입이고, 다른 타입으로 변환하려면 Type을 지정해줘야한다.
        String url = env.getProperty("my.datasource.url");
        String username = env.getProperty("my.datasource.username");
        String password = env.getProperty("my.datasource.password");
        int maxConnection = env.getProperty("my.datasource.etc.max-connection", Integer.class);
        Duration timeout = env.getProperty("my.datasource.timeout", Duration.class);
        List<String> options = env.getProperty("my.datasource.etc.options", List.class);

        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }


}
