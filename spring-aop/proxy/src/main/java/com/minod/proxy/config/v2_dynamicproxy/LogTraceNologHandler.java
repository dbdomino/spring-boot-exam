package com.minod.proxy.config.v2_dynamicproxy;

import com.minod.proxy.logtracer.LogTracer;
import com.minod.proxy.logtracer.TraceStatus;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 핸들러, InvocationHandler 상속받아 구현하는게 핵심.
// LogTraceBasicHandler로 로그트레이서가 필요한 곳에 적용 가능한 프록시 구현체다.
public class LogTraceNologHandler implements InvocationHandler {
    private final Object target;
    private final LogTracer logTracer;
    private final String[] patterns;
    public LogTraceNologHandler(Object target, LogTracer logTracer, String[] patterns) {
        this.target = target;
        this.logTracer = logTracer;
        this.patterns = patterns;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 메서드 이름 필터 save, request, reque*, *est 패턴에 맞는걸로 쓸거임.
        String methodNameFilter = method.getName();
        // 특정 메서드 이름이 매칭되는 경우에만 LogTracer로직 실행, 매칭 안되면 LogTracer 로직 실행안함.
        // patterns, 은 맴버변수로 설정해줘야 하는데, 외부에서 패턴도 넣어주는걸로 퉁침, 패턴에 맞게 들어오는지 String으로 검증 가능.
        /**스프링이 제공하는 PatternMatchUtils.simpleMatch(..) 를 사용하면 단순한 매칭 로직을 쉽게 적용할수 있다.
         xxx : xxx가 정확히 매칭되면 참
         xxx* : xxx로 시작하면 참
         *xxx : xxx로 끝나면 참
         *xxx* : xxx가 있으면 참
         */
        if (!PatternMatchUtils.simpleMatch(patterns, methodNameFilter)) {
            return method.invoke(target, args);
        }

        TraceStatus status=null;
        try {
            String methodToSting = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()"; // 클래스명.메서드명()
            // Bean으로 등록하면서, invoke() 가 한번 수행되었다...
            status = logTracer.begin("LogTraceBasicHandler methodToSting : "+methodToSting+" 메타정보라 이렇게 가져오는게 가능");

            // 메서드 동적 호출하려면? 여긴 JDK 프록시 핸들러의 invoke안이다. target의 메서드를 호출해야함.
            // Message는 target객체의 메타 객체를 넣어줬을 것이다. 아닐 경우 런타임 에러 발생한다. 컴파일 에러로는 못잡음.
            // 믿고, Message가 지원하는 invoke(target객체, 인자)를 써야함.
//            result = target.request(itemId);
            Object result = method.invoke(target, args);// method.invoke(target) 인자 없으면 이렇게도 가능

            logTracer.end(status);

            return result; // 이런식으로 프록시에서 작동 한다.
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
