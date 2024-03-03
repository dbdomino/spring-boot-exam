package com.minod.aws;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.io.IOException;
import java.util.Map;

// 스프링부트 실행 하면서 추가하기

public class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources(); // application.Properties  근본

        try {
            /* 외부 config 파일 불러와서 추가하기
            ClassPathResource resource = new ClassPathResource("custom.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("customProperties", properties);

            propertySources.addFirst(propertySource);*/

            String arn0 = environment.getProperty("aws.secret.arn");
//            System.out.println(arn0);
            // 시크릿매니저 불러오기
            AWSSecretsManager secretsManager = AWSSecretsManagerClientBuilder.defaultClient();
            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(arn0);
            String secretString = secretsManager.getSecretValue(getSecretValueRequest).getSecretString();

            Map<String, String> secretMap;
            // 값 프로퍼티에 추가
            secretMap = new ObjectMapper().readValue(secretString, new TypeReference<>() {});
                for (String key : secretMap.keySet()) {

                    String value = secretMap.get(key);
//                    System.out.println("key : "+key+", value : "+value);
                    System.setProperty(key, value); // 미리 값 프로퍼티에 추가.
                }

//            propertySources.addFirst();
//            propertySources.addFirst(secretMap);


        } catch (IOException e) {
            // 예외 처리
        }
    }
}
