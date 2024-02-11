package com.minod.core.beanfind;

import com.minod.core.config.Appconfig;
import com.minod.core.discount.DiscountPolicy;
import com.minod.core.discount.FixDiscountPolicy;
import com.minod.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName(" 부모 타입으로 조회시, 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByparentTypeDuplicate() {
//        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);

        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName(" 부모 타입으로 조회시, 자식이 둘 이상 있으면, 빈이름으로 지정하기.")
    void findBeanByparentTypeBeanName() {
//        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);

//        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }


    @Test
    @DisplayName(" 특정 하위 타입으로 조회.")
    void findBeanBySubType() {
//        DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);

        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기") // 자식타입인 객체 두개가 TestConfig에 등록되어 있으며, 이들이 조회된다.
    void findAllBeanByParentType() {
        // ac.getBeansOfType  이게 머냐???
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);

        assertThat(beansOfType.size()).isEqualTo(2);

        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value=" + beansOfType.get(key));
        }
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object") // Object로 조회하면 하위 자식 bean들이 모두 조회된다. Object 는 최상위 객체이므로
    void findAllBeanByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }


    @Configuration
    static class TestConfig {

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();

        }
        @Bean DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
