package com.minod.core.beanfind;

import com.minod.core.config.Appconfig;
import com.minod.core.service.MemberService;
import com.minod.core.service.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);

    @Test
    @DisplayName("빈 이름으로 컨테이너에서 조회")
    void findBeanByName() { // junit5 부터 public 안써도된다.
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함

    }

    @Test
    @DisplayName("이름 없이 타입으로 컨테이너에서 조회")
    void findBeanByType() { // junit5 부터 public 안써도된다.
        MemberService memberService = ac.getBean( MemberService.class);
// 역할에 의존한 검색
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함

    }

    @Test
    @DisplayName("구체적인 타입명으로 컨테이너에서 조회")
    void findBeanByType2() { // junit5 부터 public 안써도된다.
        MemberService memberService = ac.getBean( "memberService",MemberServiceImpl.class);
// 구현에 의존한 검색
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함

    }

    @Test
    @DisplayName("빈 이름으로 컨테이너에서 조회 실패")
    void findBeanByNameX() { // junit5 부터 public 안써도된다.
 /*       MemberService memberService = ac.getBean("xxxxx", MemberService.class);

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); // 실패날 테스트
        // org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'xxxxx' available
        */

        // 실패테스트 작성하기
//        위 소스 대신해서 아래 하나로 대체 가능, jupiter.api.Assertions의 assertThrows 사용해서 에러나서 throws 되면 성공으로 뜸.
        // thorws 안되면 실패
//        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxxx", MemberService.class));
        // ondemand 를 써서 assertThrows 로 줄여서 써도됨.
        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxxx", MemberService.class));
//        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("memberService", MemberService.class));

    }

//    @Test
    @DisplayName("구체적인 타입명으로 컨테이너에서 조회")
    void findBeanByType2X() { // junit5 부터 public 안써도된다.
        MemberService memberService = ac.getBean( "memberService",MemberServiceImpl.class);
// 구현에 의존한 검색
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class); // alt + enter 눌러서 디맨드 온 스태틱 해주면 편함

    }
}
