package com.minod.core.beandefinition;

import com.minod.core.config.Appconfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
    //    GenericXmlApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
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
