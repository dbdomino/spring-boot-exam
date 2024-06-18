package hello.helloenv.common;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// 제일중요

@Slf4j
@Component
public class EnvironmentCheck {
    private final Environment env;

    public EnvironmentCheck(Environment environment) {
        this.env = environment;
    }

    @PostConstruct
    public void init() {
        String url= env.getProperty("url", "없을때 값 url");
        String dbuser = env.getProperty("dbuser");
        String dbpw = env.getProperty("dbpw");
        String username = env.getProperty("username","nonUser");

        log.info("url : {}", url);
        log.info("dbuser : {}", dbuser);
        log.info("dbpw : {}", dbpw);
        log.info("username : {}", username);
    }
}
