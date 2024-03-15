package com.minod.proxy.config.v5_autoproxycreate;

import com.minod.proxy.config.AppV1Config;
import com.minod.proxy.config.AppV2Config;
import com.minod.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import com.minod.proxy.logtracer.LogTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // V3는 컴포넌트 스캔으로 자동으로 스프링 빈으로 등록되지만, V1, V2 애플리케이션은 수동으로 스프링 빈으로 등록해야 동작한다
public class AutoProxyCreatorConfig {
    // 자동생성되는 스프링 빈 후처리기  AutoProxyCreator

//    @Bean // 빈 후처리기, 스프링이 제공하는 것으로 사용하여 이건 해제
//    public PackageLogTracePostProcessor logTracePostProcessor(LogTracer logTracer){
//        return new PackageLogTracePostProcessor("com.minod.proxy.app", getAdvisor(logTracer));
//    }

//    @Bean // Advisor를 Bean으로 등록 해줘야 함. Bean으로 쓰려면 private가 아닌 public
    public Advisor getAdvisor(LogTracer logTracer) {
        // Advisor 반환하려면? Advisor의 조건으로 Pointcut과 Advice 필요
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice   스프링 프록시기반 공통로직 추가를 위해 MethodInterceptor 구현체(advice) 작성
        LogTraceAdvice advice = new LogTraceAdvice(logTracer);// 공통로직을 위해 logTracer 주입필요. 초기 주입은 Config에서 시작이네

        // return advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }


    // AspectJExpressionPointcut
    // AspectJ라는 AOP에 특화된 포인트컷 표현식을 적용할 수 있다. AspectJ 포인트컷 표현식 알아야 함.
    // Advisor만 추가하는 것으로 스프링에서 프록시 기능을 사용할 수 있는 것이다. 위 getAdvisor() 를 대체
//    @Bean
    public Advisor getAdvisor2(LogTracer logTracer) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.minod.proxy.app..*(..))"); // AspectJ표현식으로
        LogTraceAdvice advice = new LogTraceAdvice(logTracer);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // AspectJExpressionPointcut
    @Bean
    public Advisor getAdvisor3(LogTracer logTracer) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // noLog 메서드 빼고 모두 되도록
        pointcut.setExpression("execution(* com.minod.proxy.app..*(..)) && !execution(* com.minod.proxy.app..noLog(..))"); // AspectJ표현식으로
        LogTraceAdvice advice = new LogTraceAdvice(logTracer);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
