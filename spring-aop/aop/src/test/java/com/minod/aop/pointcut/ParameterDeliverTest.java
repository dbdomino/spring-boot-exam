package com.minod.aop.pointcut;

import com.minod.aop.pointcuts.MemberService;
import com.minod.aop.pointcuts.annotation.ClassAop;
import com.minod.aop.pointcuts.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 다음은 포인트컷 표현식을 사용해서 어드바이스에 매개변수를 전달할 수 있다.
 * this, target, args,@target, @within, @annotation, @args
 *
 * 포인트컷의 매개변수 이름과, Advice에 넣을 매개변수의 이름을 맞추어야 한다. (다르면 작동안함)
 * 추가로 타입이 메서드에 지정한 타입으로 제한된다.
 *
 * @Before("allMember() && args(arg,..)")
 * public void logArgs3(String arg) {
 *     log.info("[logArgs3] arg={}", arg);
 * }
 * 예시에는 메서드의 타입이 String 으로 되어 있기 때문에
 * 다음과 같이 정의되는 것으로 이해하면 된다.
 *
 * logArgs1 : joinPoint.getArgs()[0] 와 같이 매개변수를 전달 받는다.
 * logArgs2 : args(arg,..) 와 같이 매개변수를 전달 받는다.
 * logArgs3 : @Before 를 사용한 축약 버전이다. 추가로 타입을 String 으로 제한했다.
 * this : 프록시 객체를 전달 받는다.
 * target : 실제 대상 객체를 전달 받는다.
 * @target , @within : 타입의 애노테이션을 전달 받는다.
 * @annotation : 메서드의 애노테이션을 전달 받는다. 여기서는 annotation.value() 로 해당 애노테이션의 value로 등록한 값을 가져올 수 있다.
 *
 */

@Slf4j
@Import(ParameterDeliverTest.ParameterAspect.class)
@SpringBootTest
public class ParameterDeliverTest {
    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA"); // 여기 파라미터 "helloA"를 프록시에서 받아 Advice로직에 넣는방법?

    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* com.minod.aop.pointcuts..*.*(..))")
        private void allMember() {}

        // 1. 단순한 방법
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable{
            Object args = joinPoint.getArgs()[0]; // 배열에서 직접 꺼내기
            log.info("logArgs1 logArgs1 {}", joinPoint.getSignature());
            log.info("logArgs1 joinPoint.getArgs()[0]: {}", args);
            return joinPoint.proceed();
        }

        // 2. args 지시자 사용하기, args 지시자에 들어가는 파라미터를 Advice의 파라미터와 맞춰 사용함.
        @Around("allMember() && args(momon, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object momon) throws Throwable{

            log.info("logArgs2 logArgs1 {}", joinPoint.getSignature());
            log.info("logArgs2 joinPoint.getArgs()[0]: {}", momon);
            return joinPoint.proceed();
        }

        // 3. args 지시자 사용하기,코드 줄이기.
        @Before("allMember() && args(momon, ..)")
        public void logArgs3(String momon) {
            log.info("logArgs3 momon: {}", momon);
        }

        // 4. 안끝났다. this로 가져오기 (인스턴스를 가져온다.)
        // this는 CGLIB로 프록시로 만들어진 객체를 바라봄
        @Before("allMember() && this(obj)")
        public void logArgs4(JoinPoint joinPoint, MemberService obj) {
            // joinPoint는 안써도 됨.
            log.info("logArgs4 this(obj) : {}", obj);
            log.info("logArgs4 this(obj).getClass() : {}", obj.getClass());
        }

        // 5. 안끝났다. target으로 가져오기 (인스턴스를 가져온다.)
        // 프록시 호출하는 실제 대상 객체를 바라봄.
        @Before("allMember() && target(obj)")
        public void logArgs5(JoinPoint joinPoint, MemberService obj) {
            // joinPoint는 안써도 됨.
            log.info("logArgs5 target(obj) : {}", obj);
            log.info("logArgs5 target(obj).getClass() : {}", obj.getClass());
        }

        // 6. 아직 안끝났다. @target으로 어노테이션 정보 Advice로 가져오기 (클래스의 어노테이션에 붙은걸로 가져오는 것)
        //
        @Before("allMember() && @target(annotation)")
        public void logArgs6(JoinPoint joinPoint, ClassAop annotation) { // @ClassAop라는 커스텀 어노테이션이 MemberServiceImpl에 붙어있음. 클래스위에
            // joinPoint는 안써도 됨.
            log.info("logArgs6 @target(annotation)의 어노테이션정보 : {}", annotation);
        }

        // 7. @within으로 어노테이션 정보 Advice로 가져오기 (클래스의 어노테이션에 붙은걸로 가져오는 것)
        //
        @Before("allMember() && @within(annotation)")
        public void logArgs7(JoinPoint joinPoint, ClassAop annotation) { // @ClassAop라는 커스텀 어노테이션이 MemberServiceImpl에 붙어있음. 클래스위에
            // joinPoint는 안써도 됨.
            log.info("logArgs7 @within(annotation)의 어노테이션정보 : {}", annotation);
        }

        // 8. @annotation으로 메서드에 붙은 어노테이션 정보 Advice로 가져오기 (클래스의 어노테이션에 붙은걸로 가져오는 것)
        //
        @Before("allMember() && @annotation(annotation)")
        public void logArgs8(JoinPoint joinPoint, MethodAop annotation) { // @MethodAop 커스텀 어노테이션이 hello 메서드에 붙어있음.
            // joinPoint는 안써도 됨.
            log.info("logArgs8 @annotation(annotation)의 어노테이션정보 : {}", annotation);
            log.info("logArgs8 @annotation(annotation) 의 어노테이션 value : {}", annotation.value());// 한글로된 value도 가져올 수 있다.
            // 어노테이션 정보에 선언한 value까지 가져올 수 있긴하네...
        }


    }

}
