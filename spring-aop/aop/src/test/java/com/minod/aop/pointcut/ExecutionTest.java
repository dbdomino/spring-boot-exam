package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {
    // 포인트컷에서 자주 사용되는 AspectJ 표현식의 execution 문법을 테스트해보자.
    // AspectJExpressionPointcut 이 바로 포인트컷 표현식을 처리해주는 클래스다. 여기에 포인트컷 표현식을 지정
    // 하면 된다. AspectJExpressionPointcut 는 상위에 Pointcut 인터페이스를 가진다.
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);

    }

    @Test
    void printMethod() {
        // MemberServiceImpl.hello(String) 메서드의 정보를 출력해준다.
        log.info("메타정보로 가져온 helloMethod={}", helloMethod);
        // public java.lang.String com.minod.aop.pointcuts.MemberServiceImpl.hello(java.lang.String) 이렇게나옴.

    }

    @Test
    void execution(){
        // 쌍따옴표안이라 컴파일체크가 안됨. 오타주의
        // 생략없이 모든 내용 표현
        pointcut.setExpression("execution(public String com.minod.aop.pointcuts.MemberServiceImpl.hello(String))");

        //AssertThat 쓰는이유? 맞는지 틀린지 테스트하려고.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // matches 가 true반환하는지 확인
        /** pointcut.matches(메서드, 대상 클래스) 를 실행하면 지정한 포인트컷 표현식의 매칭 여부를 true, false 로 반환한다.
         * 매칭 조건 (다맞아야 true)
         * 접근제어자?: public
         * 반환타입: String
         * 선언타입(클래스명/패턴)?: hello.aop.member.MemberServiceImpl
         * 메서드이름: hello
         * 파라미터: (String)
         * 예외?: 생략
         */
    }

    @Test
    void allMatch() {
        // 모든 내용을 생략함.
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
        /**
         * 매칭조건 (생략한건 검증안하는 것이겠지.)
         * 접근제어자?: 생략
         * 반환타입: *
         * 선언타입(클래스명/패턴)?: 생략
         * 메서드이름: *
         * 파라미터: (..)     파라미터에서 .. 은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다.
         * 예외?: 없음
         */
    }

    // 메서드명 관련 매칭
    @Test
    @DisplayName("메서드패턴 매치1")
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("메서드패턴 매치2")
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("메서드패턴 매치3")
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("메서드이름 매치실패")
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지 정확히 매치")
    void packageExactMatch1() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("패키지경로의 모든것과 매치1")
    void packageExactMatch2() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("패키지경로의 모든것과 매치 실패(하위포함이 아님)")
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* com.minod.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod,
                MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지경로의 하위포함 매치1 .. ")
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("패키지경로의 하위포함(하위의 모든 패키지) 매치2 .. ")
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* com.minod..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 부모타입 인터페이스로 패턴매치 검증시 매치 됨.
    @Test
    @DisplayName("패키지 클래스 정확히 매치1")
    void typeExactMatch() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("패키지 클래스 부모타입 매치1 가능 ")
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    @Test
    @DisplayName("패키지 클래스 내부메서드")
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }
    @Test
    @DisplayName("패키지 클래스 부모타입 패턴으로, 내부메서드는 매칭불가")
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.minod.aop.pointcuts.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    /** execution 파라미터 매칭 규칙은 다음과 같다.
     (String) : 정확하게 String 타입 파라미터
     () : 파라미터가 없어야 한다.
     (*) : 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
     (*, *) : 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
     (..) : 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 참고로 파라미터가 없어도 된다. 0..* 로 이해하면 된다.
     (String, ..) : String 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
       ex) (String) , (String, Xxx) , (String, Xxx, Xxx) 허용
     */
    //String 타입의 파라미터 허용
    @Test
    @DisplayName("파라미터 타입 매치")
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    //파라미터가 없어야 함
    @Test
    @DisplayName("파라미터 없어야 true, 있으면 false")
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
    //파라미터가 딱 하나만 있으면 성공
    @Test
    @DisplayName("파라미터 1개만 있어야 성공")
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    //숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //파라미터가 없어도 됨
    //(), (Xxx), (Xxx, Xxx)
    @Test
    @DisplayName("파라미터 1개만 있어야 성공")
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    //String 타입으로 시작하기만 하면 ok, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //(String), (String, Xxx), (String, Xxx, Xxx) 허용
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
