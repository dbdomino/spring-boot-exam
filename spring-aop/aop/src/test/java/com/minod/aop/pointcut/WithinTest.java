package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class WithinTest {
    // 포인트 컷에서 within 이라는 지시자 사용 : 특정 타입 내의 조인 포인트들로 매칭을 제한한다.
    // 해당 타입이 매칭되면 그 안의 메서드(조인 포인트)들이 자동으로 매칭된다.
    // execution 에서 클래스명 부분만 사용한다고 보면 된다.

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    @DisplayName("클래스명(타입) 정확히 일치")
    void withinExact() {
        pointcut.setExpression("within(com.minod.aop.pointcuts.MemberServiceImpl)");
        //
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("클래스명(타입) 패턴 지원")
    void withinStar() {
        pointcut.setExpression("within(com.minod.aop.pointcuts.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("클래스명(타입) 패턴 하위경로 포함")
    void withinSubPackage() {
        pointcut.setExpression("within(com.minod.aop..*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // within 사용시 주의해야 할 점이 있다. 표현식에 부모 타입을 지정하면 안된다는 점이다.
    // 정확하게 타입이 맞아야 한다. 이 부분에서 execution 과 차이가 난다.
    @Test
    @DisplayName("within에선 부모타입 인터페이스로 패턴체크 안됨.")
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(com.minod.aop.pointcuts.MemberService)");
        assertThat(pointcut.matches(helloMethod,
                MemberServiceImpl.class)).isFalse();
    }
    @Test
    @DisplayName("execution은 타입기반, 부모타입 인터페이스로 패턴체크 가능.")
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
