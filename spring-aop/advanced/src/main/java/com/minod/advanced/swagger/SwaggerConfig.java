package com.minod.advanced.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

/**  여기로 접속하면 됨.
 *  http://localhost:8080/swagger-ui.html
 *
 *  https://springdoc.org/v2/ 공식문서에서 제공하는 dependency로 설치를 했습니다.
 *  springdoc 라이브러리가 OpenAPI3.0 스펙에 맞는 JSON을 만들어주면, Swagger UI가 화면을 만들어서 JSON들을 띄워주는 역할을 합니다.
 *  springdoc-openapi-ui를 살펴보면 swagger-ui를 포함하고 있는 것을 확인할 수 있습니다.
 *
 *  swagger-ui : 핵심 로직 구현
 *  springdoc-openapi : swagger-ui를 추상화해서 더 잘 활용할 수 있게 해주는 라이브러리
 *
 *  아래의 컨피그 파일 없이도 위 URL을 통해서 현재 개발되어 있는 api 스펙을 알 수 있다.
 *  설치 가이드 https://resilient-923.tistory.com/414
 *  사용 가이드 https://kim-jong-hyun.tistory.com/49
 */

@OpenAPIDefinition(
        info = @Info(title = "Couple App",
                description = "couple app api명세",
                version = "v1"))
@RequiredArgsConstructor
//@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("COUPLE API v1")
                .pathsToMatch(paths)
                .build();
    }
}