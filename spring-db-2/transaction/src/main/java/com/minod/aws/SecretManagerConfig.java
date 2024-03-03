package com.minod.aws;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

// Configuration으로 프로퍼티에 값 추가하기
@Configuration
@Order(0) // 실행 순서를 지정하기 위한 어노테이션
public class SecretManagerConfig {
    @Value("${aws.secret.arn}") // 스프링이 시작되기 전이라서...
    private String secretArn;

    @Bean
    public AWSSecretsManager awsSecretsManager() {
        return AWSSecretsManagerClientBuilder.defaultClient();
    }

    //    @EventListener(ApplicationReadyEvent.class) // Config가 아니라,
    //자동주입 으로 생성되는 것보다 먼저 수행되어야 한다.
//    @Bean
    public void mySecretProperty() throws IOException {
//    public String mySecretProperty() throws IOException {
//    public String mySecretProperty(AWSSecretsManager awsSecretsManager) {
        System.out.println(secretArn); // 테스트에선 먼저 선언될 때 @Value로 읽어오질못함.
        // Json으로 받아오기.
//        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretArn);
//        System.out.println(awsSecretsManager.getSecretValue(getSecretValueRequest).getSecretString()); // {"keySample":"valueSample","minod_pw":"minodminod","minod_id":"minod"}
//        return awsSecretsManager.getSecretValue(getSecretValueRequest).getSecretString();

        // 프로퍼티 경로 입력해서 가져오기
        // ClassPathResource를 사용하여 프로퍼티 파일을 로드합니다.
        ClassPathResource resource = new ClassPathResource("application.properties");
        // PropertiesLoaderUtils를 사용하여 프로퍼티를 로드합니다.
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        // 프로퍼티 값들을 출력합니다.
        /*properties.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });*/
        String arn1 = properties.getProperty("aws.secret.arn");
        String arn2 = properties.get("aws.secret.arn").toString(); // arn1 arn2 는 같은값.

        // 시크릿매니저에서 불러오기
        System.out.println("ARN : "+arn2);
        AWSSecretsManager secretsManager = AWSSecretsManagerClientBuilder.defaultClient();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(arn2);
        String secretString = secretsManager.getSecretValue(getSecretValueRequest).getSecretString();

        // 값 프로퍼티에 추가
        try {
            Map<String, String> secretMap = new ObjectMapper().readValue(secretString, new TypeReference<>() {});
            for (String key : secretMap.keySet()) {

                String value = secretMap.get(key);
                System.out.println("key : "+key+", value : "+value);
                System.setProperty(key, value);
            }
        } catch (RuntimeException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        return "";
    }
}
