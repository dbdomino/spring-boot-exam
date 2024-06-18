package com.hello;

import com.hello.config.MelloConfig;
import com.hello.config.MelloSelector;
import com.hello.domain.MelloServe;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

public class ImportSelectorTest {
    @Test
    void staticConfig() {
        // AnnotationConfigApplicationContext 스프링 컨테이너 만들기
        // 다이렉트로 빈 등록
        AnnotationConfigApplicationContext appContext =
                new AnnotationConfigApplicationContext(MelloConfig.class);

        MelloServe bean = appContext.getBean(MelloServe.class);
        assertThat(bean).isNotNull();
    }
    @Test
    // 돌려서 selector를 활용한 동적으로 빈 등록
    void selectorConfig() {
        AnnotationConfigApplicationContext appContext =
                new AnnotationConfigApplicationContext(StaticConfig.class);
        MelloServe bean = appContext.getBean(MelloServe.class);
        assertThat(bean).isNotNull();
    }
    @Configuration
    @Import(MelloSelector.class)
    public static class StaticConfig {
    }

}
