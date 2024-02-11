package com.minod.core.autowired;

import com.minod.core.member.Member;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
        // SpringContainer 올라올때 Autowired다 호출되므로 sout 돌아가는지 확인하기

    }


    static class TestBean {

        // Member는 스프링 Bean이아님

        //호출 안됨
        @Autowired(required = false) // Autowiried시킬 Member가 지정되지않은상태이며 컴포넌트스캔에도 없다보니 required=false 안해주면 에러남. UnsatisfiedDependencyException
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1 = " + member);
        }
        //null 호출
        @Autowired //null로 넣을수있도록 설정하여, Autowiried시킬 Member가 지정되지않은상태에서 Null로 반환
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 = " + member);
        }
        //Optional.empty 호출
        @Autowired(required = false)  //Optional<Member> 로 받도록하여 Autowiried시킬 Member가 지정되지않은상태에서 Optional에 null이 들어간채로 반환된다.
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }
    }
}
