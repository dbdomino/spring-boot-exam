package com.minod.core.beanscope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {


    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        Assertions.assertThat(prototypeBean1).isNotEqualTo(prototypeBean2);  // 다르다고 나옴 equals
        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);   // 다르다고 나옴 ==

        ac.close();

    }

    @Scope("prototype") // 프로토타입 스코프로 된다면, Bean으로 등록되더라도 인스턴스를 싱글톤으로 제공하지 않으며, 종료도 알아서안해주므로 클라이언트가 종료시켜야 된다.
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");

        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
