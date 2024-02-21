package com.exprocess.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    // HandlerExceptionResolver 를 직접 구현해서 사용해야 한다. 이름길다 외우기싫다.
    // Bean으로 등록해줘야 하긴 한데, WebMvcConfigurer 구현체인 설정파일에다가 Override 해줘야한다.
    // 메소드 : extendHandlerExceptionResolvers

    /**
     * 빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지않고, 정상(원래) 흐름으로 서블릿이 리턴된다.
     * ModelAndView 지정: ModelAndView 에 View, Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
     * null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는 ExceptionResolver 가 없으면
     *       예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다. (그냥 예외가 처리안되서 날아감)
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) { // 오류가 IllegalArgumentException라면, 응답 400으로 바꿔서 보내는 것도 가능
                log.info("IllegalArgumentException resolver to 400");
                log.info("MyHandler resolver AAA");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); // SC_BAD_REQUEST 400 이게 핵심
                return new ModelAndView(); //여기선 빈 ModelAndView 반환, 정상응답 으로 보내야하나, 위의 sendError로 오류상태로 전달하여 다시 오류페이지 요청옴.
            }
            // ModelAndView에 값을 채워서 새로운 뷰를 렌더링 시키도록 할 수도 있다.
        } catch (Exception e) {
            log.error("resolver ex", e);
        }
        log.info("MyHandler resolver BBB");
        return null;
    }
}
