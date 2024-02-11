package com.minod.core.beanfind;

import com.minod.core.config.Appconfig;
import com.minod.core.repository.MemberRepository;
import com.minod.core.repository.MemoryMemberRepository;
import com.minod.core.service.MemberService;
import com.minod.core.service.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextSameBeanFindTest {

//    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() { // junit5 부터 public 안써도된다.
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name=" + beanDefinitionName + " object=" + bean);

        }
    }

    @Test
    @DisplayName("타입으로 조회시 같은 타입?이 둘 이상 있으면, 중복 오류 발생")
    void findBeanByType2X() { // junit5 부터 public 안써도된다.
        MemberRepository memberRepository = ac.getBean( "memberRepository1", MemberRepository.class); // 성공
//        MemberRepository memberRepository2 = ac.getBean( MemberRepository.class); // 실패
        // org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.minod.core.repository.MemberRepository' available: expected single matching bean but found 2: memberRepository1,memberRepository2

// 구현에 의존한 검색
//        assertThat(memberRepository).isInstanceOf(MemberRepository.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함
//        assertThat(memberRepository2).isInstanceOf(MemberRepository.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함

// 실패, 에러내므로 성공으로 뜸.
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean( MemberRepository.class));

    }

    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면, 빈 이름을 지정하면 된다")
    void findBeanByName() {
        MemberRepository memberRepository = ac.getBean("memberRepository1",
                MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }
    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    void findAllBeanByType() {
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class); // Map으로 나온다.

        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " +
                    beansOfType.get(key));
        }
        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }



    @Configuration
    static class SameBeanConfig {
        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
}
