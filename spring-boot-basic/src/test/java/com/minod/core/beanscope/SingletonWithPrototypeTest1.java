package com.minod.core.beanscope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); // protoType Bean을 가져와서
        prototypeBean1.addCount(); //count++

        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); // protoType Bean을 가져와서
        prototypeBean2.addCount(); // count++

        assertThat(prototypeBean2.getCount()).isEqualTo(1); // 각각 값이 증가해야한다. 싱글톤이 아니기에
    }

    @Test
    void providerTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);  // SingletonWithPrototypeTest1$PrototypeBean@560348e6 // 싱글톤 빈 안에 계속 새로운

        int count1 = clientBean1.logic();  // SingletonWithPrototypeTest1$PrototypeBean@6d07a63d
        clientBean1.init();

        assertThat(count1).isEqualTo(1); // SingletonWithPrototypeTest1$PrototypeBean@6d07a63d

        // 여기를보면 ClientBean은 싱글톤 빈이 맞나?
        ClientBean clientBean2 = ac.getBean(ClientBean.class);  // SingletonWithPrototypeTest1$PrototypeBean@51133c06
        clientBean2.init();

        int count2 = clientBean2.logic();  // SingletonWithPrototypeTest1$PrototypeBean@4b213651

        assertThat(count2).isEqualTo(2);  // 공통된 prototypeBean 을 사용
    }

    @Test
    void providerTest2() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean2.class, PrototypeBean.class);
        ClientBean2 clientBean1 = ac.getBean(ClientBean2.class);  // SingletonWithPrototypeTest1$PrototypeBean@560348e6 // 싱글톤 빈 안에 계속 새로운

        int count1 = clientBean1.logic();  //

//        assertThat(count1).isEqualTo(1);

        // 여기를보면 ClientBean은 싱글톤 빈이 맞나?
        ClientBean2 clientBean2 = ac.getBean(ClientBean2.class);  //

        int count2 = clientBean2.logic();  //

//        assertThat(count2).isEqualTo(1);
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy ");
        }
    }

    @Scope("singleton")
    static class ClientBean {
        private final PrototypeBean prototypeBean; // DI로 의존성 주입시켜 같은 prototypeBean을 공유하므로 count value도 공유함. x01

        public ClientBean(PrototypeBean prototypeBean){    // 의존성 주입시, 값이 변경될 수 있는 value를 Bean에서 쓰는건 피해야하며, Bean외부에서 관리되도록 해야한다.
            this.prototypeBean = prototypeBean;
        }

        @Autowired
        private ApplicationContext ac; //

        @PostConstruct
        public void init() {
            System.out.println("singleton.init " + this);
        }

        public int logic() {   // 싱글톤 Bean 안에 프로토타입 Bean을 가지고 있는 것이다. logic()을 호출하면 prototypeBean을 만들고 addCount()호출하며, count를 반환
            // 해결책 ac.getBean으로 매번 새로운 프로토타입 Bean을 생성함.
            // 의존관계를 외부에서 주입(DI) 받는게 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 Dependency Lookup //(DL) 의존관계 조회(탐색) 이라한다.
            // 문제는 이 Bean들이 계속 남아있을건데....
//            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);

            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("singleton")
    static class ClientBean2 {
        /*private final PrototypeBean prototypeBean; // DI로 의존성 주입시켜 같은 prototypeBean을 공유하므로 count value도 공유함. x02

        public ClientBean2(PrototypeBean prototypeBean){    // 의존성 주입시, 값이 변경될 수 있는 value를 Bean에서 쓰는건 피해야하며, Bean외부에서 관리되도록 해야한다.
            this.prototypeBean = prototypeBean;
        }*/

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider; // jakarta.inject 의 Provider 를 사용

        public int logic() {   // prototypeBeanProvider.get() get() 메서드 하나뿐이며, prototype 객체를 여러개 생성할 수 있도록 하게해주는 것...
            // 프로토타입 빈이 필요하다면, 사용하는 게 낫다만 주의해서 사용하자. 프로토타입 빈의 종료는 자동으로 지원되지 않으니까.
            PrototypeBean prototypeBean = prototypeBeanProvider.get();

            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

}
