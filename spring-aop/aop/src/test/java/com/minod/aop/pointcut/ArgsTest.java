package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ArgsTest {
    // args : 인자가 주어진 타입의 인스턴스인 조인 포인트로 매칭
    //기본 문법은 execution 의 args 부분과 같다.
    // 다만 execution 은 파라미터 타입이 정확하게 매칭되어야 하며, args는 부모타입 파라미터를 허용한다.
    // args 는 실제 넘어온 파라미터 객체 인스턴스를 보고 판단한다.(Object로 선언해두는 것도 가능하단 소리)

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression); // 포인트 컷에 expression set하고 실행되면 못바꿈.
        // 포인트컷 표현식을 넣어 만들어 쓰는 메서드.
        return pointcut;
    }

    @Test
    void args() {
        //hello(String)메서드와, args 지시자 매칭 (파라미터에 부모타입이 오더라도 허용된다.)
        assertThat(pointcut("args(String)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args()").matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("args(..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(*)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(String,..)").matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void execution() {
        //hello(String)메서드와, execution 지시자 매칭 (파라미터에 선언된게 정확한 타입으로 와야한다..)
        assertThat(pointcut("execution(* *(String))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(Object))").matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *())").matches(helloMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *(..))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(*))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(String,..))").matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
