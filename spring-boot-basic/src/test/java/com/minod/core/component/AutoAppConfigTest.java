package com.minod.core.component;

import com.minod.core.AutoAppConfig;
import com.minod.core.repository.MemberRepository;
import com.minod.core.service.MemberService;
import com.minod.core.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoAppConfigTest {
    @Test
    void basicScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class); // componentScan을 수행시키려면 프로젝트 최상단에 AutoAppConfig파일이 위치해야한다.
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        // AutoAppConfig 의 ac에는 먼가 확인되지 않음. @Component로 각 클래스에 설정한것도 자동으로 불러와지지 않음.

        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);

        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = orderService.getMemberRepository();
        System.out.println("memberRepository = " + memberRepository);


    }

    @Test
    @DisplayName("MemberRepository 테스트 수동등록 자동등록 충돌")
    void basicScan2() {
        // 여기서 수동 등록 Bean이 더 우선순위를 가져서 자동등록 Bean을 Override시킨다고 한다.
        // 자동등록 Bean이란 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는 것을 말한다.
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberRepository memberRepository = ac.getBean(MemberRepository.class);

        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();

//        String[] beanDefinitionNames = ac.getBeanDefinitionNames();   // 이거 자주쓰이네;
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                // beanDefinitionName 이건 메서드명, 빈이름        beanDefinition 은 빈 정보, Generic Bean / Root Bean 인지, 객체 정보, factoryBeanName factoryMethodName 다볼수있음.
                System.out.println("beanDefinitionName : " + beanDefinitionName +",       beanDefinition = " + beanDefinition);
            }
        }
    }
}
