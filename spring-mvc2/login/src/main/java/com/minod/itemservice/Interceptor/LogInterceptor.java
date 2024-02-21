package com.minod.itemservice.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    private static final String LOG_ID = "logId";
    // 인터셉터도 싱글턴으로 만들어진다.
    // 변하는 변수가 필요하다면, request로 넘기던지 하는게 좋다. 싱글턴이므로 동기화문제 가능성있음.
    // 서블릿 필터의 경우 지역변수로 해결이 가능하지만, 스프링 인터셉터는 호출 시점이 완전히 분리되어있다.
    // 따라서 preHandle 에서 지정한 값을 postHandle , afterCompletion 에서 함께 사용하려면 어딘가에 담아두어야 한다.
    // LogInterceptor 도 싱글톤 처럼 사용되기 때문에 맴버변수를 사용하면 위험하다. 따라서 request 에 담아두었다.

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        String uuid = UUID.randomUUID().toString();  // 요청 로그를 구분하기 위한 uuid
        request.setAttribute(LOG_ID, uuid); // ctrl + alt + c -> 특정 문자열, 값을 상수로 만들 수 있다용.

        // handler 가 왜나와? 디스패처 서블릿에서 어뎁터 핸들러 호출하는데, 그거 대응하려고 그러는듯
        //  스프링을 사용하면 일반적으로 @Controller, @RequestMapping 을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 HandlerMethod 가 넘어온다.
        // /resources/static 같은 정적리소스 호출은 컨트롤러를 안탄다. 이땐, ResourceHttpRequestHandler 가 핸들러 정보로 넘어논다.
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            // 컨트롤러를 태우기 위해 사전에 돌아가는 처리
            log.info("LogInterceptor preHandle ", uuid, requestURI, handler);
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // true 이면 다음 컨트롤러 호출
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID); // 요청 로그를 구분하기 위한 uuid
        // 종료로그 afterCompletion 에 구현, 예외가 발생한 경우 postHandle가 호출되지 않기 때문이다.
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        if (ex != null) log.error("afterCompletion error !! ", ex); // ??
    }
}
