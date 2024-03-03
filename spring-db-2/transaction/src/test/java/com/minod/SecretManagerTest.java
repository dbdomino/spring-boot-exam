package com.minod;

//import com.amazonaws.regions.Region;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import com.minod.aws.SecretManagerConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class SecretManagerTest {

    @Value("${aws.secret.arn}")
    private String secretArn;

    @Value("${minod_id}")
    private String secretValue;

    @BeforeAll
    static void before() throws IOException { //  히야...DB설정 SecretManager에서 불러와 프로퍼티에 적용하려면, 초기화 작업으로 이걸 해줘야 하네...
        System.out.println("start"); //
        /*ConfigurableApplicationContext context = new SpringApplicationBuilder() // 이건 스프링을 한번더 실행시키는 거라 비효율적.
                .sources(TransactionApplication.class)
                .initializers(new CustomApplicationContextInitializer())
                .run(); // 별도 스프링 컨텍스트 실행

        context.close(); // 테스트 종료 후 애플리케이션 컨텍스트 닫기*/

        SecretManagerConfig c = new SecretManagerConfig();
        c.mySecretProperty();
    }

    @Test
    void secretARNTest() {
        System.out.println(secretArn);
    }

    @Test
    void getSecret() {
        System.out.println(secretValue);
    }
}
