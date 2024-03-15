package com.minod.proxy.config.v6_aop.aspect;

import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect  // @Aspect : 애노테이션 기반 프록시를 적용할 때 필요하다.
public class LogTracerAspect {
    // 로그트레이서 외부에서 주입.
    private final LogTracer logTrace;
    public LogTracerAspect(LogTracer logTrace) {
        this.logTrace = logTrace;
    }

    /**
     * @Around("execution(* hello.proxy.app..*(..))")
     * @Around 의 값에 포인트컷 표현식을 넣는다. 표현식은 AspectJ 표현식을 사용한다.
     * @Around 의 메서드는 어드바이스( Advice )가 된다..
     * ProceedingJoinPoint joinPoint : 어드바이스에서 살펴본 MethodInvocation invocation 과
     * 유사한 기능이다. 내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체와 어떤 메서드가 호출되었는지 정보가 포함되어 있다.
     * joinPoint.proceed() : 실제 호출 대상( target )을 호출한다.
     */
    @Around("execution(* com.minod.proxy.app..*(..))") // 이게 하나의 어드바이저. 구현 메서드. Advice 내용이 아래에 드렁간다.
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // 메서드이름 암거나 해도된다.
        TraceStatus status = null;// 로그 트레이서 객체
        try {
            // Advice 구현할 때 invoke 파라미터 invocation 과 같은 역할을 하는 joinPoint(이안에 뭔가 많이 들어있다. 동적으로 메서드 정보나 클래스정보 같은것도)
            // String methodToSting = invocation.getClass() + "." + invocation.getMethod() + "()"; // 클래스명.메서드명()
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            // 조인포인트 옵션들
//        log.info("target={}", joinPoint.getTarget()); //실제 호출 대상
//        log.info("getArgs={}", joinPoint.getArgs()); //전달인자
//        log.info("getSignature={}", joinPoint.getSignature()); //join point 시그니처

            //로직 호출
            // Object result= invocation.proceed(); // Advice invoke 안의 소스와 비슷하.다.
            Object result = joinPoint.proceed();


            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }

}
