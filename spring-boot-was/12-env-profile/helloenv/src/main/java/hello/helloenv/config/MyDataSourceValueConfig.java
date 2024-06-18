package hello.helloenv.config;

import hello.helloenv.datasource.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * 스프링에서 제공하는 @Value 를 활용해서 변수로 등록하기
 * 방법1. 멤버변수로 변수 하나마다 @Value 를 붙여줘야 한다.
 * 방법2. 파라미터로 @Value를 붙여 사용할 수 있다.
 *
 * @Value 를 사용하는 방식도 좋지만, @Value 로 하나하나 외부 설정 정보의 키 값을 입력받고, 주입 받아와야 하는 부분이 번거롭다.
 * 그리고 설정 데이터를 보면 하나하나 분리되어 있는 것이 아니라 정보의 묶음으로 되어 있다.
 * 여기서는 my.datasource 부분으로 묶여있다. 이런 부분을 객체로 변환해서 사용할 수 있다면 더 편리하고 더 좋을 것이다.
 */
@Slf4j
@Configuration
public class MyDataSourceValueConfig {
    @Value("${my.datasource.url}")
    private String url;
    @Value("${my.datasource.username}")
    private String username;
    @Value("${my.datasource.password}")
    private String password;
    @Value("${my.datasource.etc.max-connection}")
    private int maxConnection;
    @Value("${my.datasource.etc.timeout}")
    private Duration timeout;
    @Value("${my.datasource.etc.options}")
    private List<String> options;

    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(url, username, password, maxConnection, timeout,options);
    }

    @Bean
    public MyDataSource myDataSource2( @Value("${my.datasource.url}") String url,
                                       @Value("${my.datasource.username}") String username,
                                       @Value("${my.datasource.password}") String password,
                                       @Value("${my.datasource.etc.max-connection}") int maxConnection,
                                       @Value("${my.datasource.etc.timeout}") Duration timeout,
                                       @Value("${my.datasource.etc.options}") List<String> options) {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }


}
