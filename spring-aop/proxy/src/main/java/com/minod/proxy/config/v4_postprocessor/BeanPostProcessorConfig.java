package com.minod.proxy.config.v4_postprocessor;

import com.minod.proxy.config.AppV1Config;
import com.minod.proxy.config.AppV2Config;
import com.minod.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import com.minod.proxy.config.v4_postprocessor.postprocessor.PackageLogTracePostProcessor;
import com.minod.proxy.logtracer.LogTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // V3는 컴포넌트 스캔으로 자동으로 스프링 빈으로 등록되지만, V1, V2 애플리케이션은 수동으로 스프링 빈으로 등록해야 동작한다
public class BeanPostProcessorConfig {
    // 이름이 길다. 이런 뜻이다. PackageLogTrace   PostProcessor
    // 핵심은 후 처리기, PostProcessor  (pre 이전, post 이후)

    @Bean // Bean으로 등록하는데 LogTracer가 Config에 선언되어있지 않고 외부에서 주입되는 것이다보니, Autowired 안되는것으로 나타남.
    // 에러는 안남. 어짜피 실행할 때 Application 파일에서 주입 시켜줌.
    public PackageLogTracePostProcessor logTracePostProcessor(LogTracer logTracer){
        // Advisor는 Configuration에서 만들어 넣어주는 게 맞는지 모르겠다...
        return new PackageLogTracePostProcessor("com.minod.proxy.app", getAdvisor(logTracer));
    }

    private Advisor getAdvisor(LogTracer logTracer) {
        // Advisor 반환하려면? Advisor의 조건으로 Pointcut과 Advice 필요
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice   스프링 프록시기반 공통로직 추가를 위해 MethodInterceptor 구현체(advice) 작성
        LogTraceAdvice advice = new LogTraceAdvice(logTracer);// 공통로직을 위해 logTracer 주입필요. 초기 주입은 Config에서 시작이네

        // return advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
