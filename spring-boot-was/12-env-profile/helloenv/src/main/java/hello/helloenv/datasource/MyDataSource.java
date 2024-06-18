package hello.helloenv.datasource;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

@Slf4j
@Data
public class MyDataSource {
    private String url;
    private String username;
    private String password;
    private int maxConnection;

    private Duration timeout;     // Duration (기간) 변환
    private List<String> options; // application.properties 에서 콤마(,)로 문자가 나열되어 있으면 자동으로 List로 변환된다.

    public MyDataSource(String url, String username, String password, int maxConnection, Duration timeout, List<String> options) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxConnection = maxConnection;
        this.timeout = timeout;
        this.options = options;
    }

    @PostConstruct
    public void init() {
        log.info("MyDataSource init");
        log.info("url: {}", url);
        log.info("username: {}", username);
        log.info("password: {}", password);
        log.info("maxConnection: {}", maxConnection);
        log.info("timeout: {}", timeout);
        log.info("options: {}", options);
    }
    /*
    url , username , password : 접속 url, 이름, 비밀번호
        maxConnection : 최대 연결 수
        timeout : 응답 지연시 타임아웃
        options : 연결시 사용하는 기타 옵션들
     */


}
