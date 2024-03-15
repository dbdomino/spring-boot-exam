package com.minod.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {
    @Test
    void basicConfigTest() {
        // 스프링 컨테이너 만드는 소스
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class); // BeanConfig.class 를 스프링 빈으로 등록

        //A.class 형식의 빈으로 등록된 ahoho를 꺼내서 쓸 수 있다.
        /*A a = applicationContext.getBean("ahoho", A.class); //
        a.helloA();
        //B는 빈으로 등록되지 않는다. BasicConfig에 Bean으로 등록 안했으니까.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,  () -> applicationContext.getBean(B.class));
        */

        // BeanPostProcesser 빈 후처리기를 통해서 다른 객체인 B.class로 반환하도록 설정했다.
        // Configure에서 A.class 형식으로 반환하도록 됬으나, B.class로 반환된다.
        // Configure에서 public A ahoho() { } 는 변경이 없는데도 아래처럼 해야 에러가 안난다.
        // 빈 후처리기로 아예 다른 객체를 반환하는 것도 가능하다.
        B a = applicationContext.getBean("ahoho", B.class);
        a.helloB();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean(A.class));

    }

    @Slf4j
    @Configuration
    static class BeanConfig {
//        @Bean(name = "beanA") // bean 이름 설정, 안하면 메서드명으로 등록되버림.
        @Bean // bean 이름 설정, 안하면 메서드명으로 Bean이 등록됨. 인터페이스명이나 클래스명이 아님.
        public A ahoho() {
            return new A();
        }
        @Bean // 빈 후처리기 등록
        public AtoBPostProcessor postProcessor() {
            return new AtoBPostProcessor();
        }
    }

    @Slf4j
    static class A{
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    // Configuration에 A객체가 Bean으로 등록되었을 것이다.
    // 이를 B객체가 Bean으로 등록되도록 후처리기를 사용해보자. (예시다... 프록시패턴을 사용하기 위해 프록시 객체를 넣는방법으로도 쓸 수 있을 것이다.)
    @Slf4j
    static class AtoBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
}
