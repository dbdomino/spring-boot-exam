package com.exprocess.resolver;

import com.exprocess.exception.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                // HTTP 요청 해더의 ACCEPT 값이 application/json 이면 JSON으로 오류를 내려주고, 그 외 경우에는 error/
                // 빈 상태의 ModelAndView를 보내준다면 성공응답으로 HTTP상태값이 뭐가됫든 성공으로 서블릿 컨테이너가 확인한다.
                // 그렇게되면 HTTP 응답코드에 따라서 서블릿 컨테이너가 재 요청을 안하고, 여기서 최종 에러처리를 담당한다.
                // 여기서 실수로 알맞지않은 view 경로를 전달한다면 클라이언트는 에러페이지도 확인 못할 수 있으므로 주의 필요하다.

                if ("application/json".equals(acceptHeader)){
                    log.info("UserException resolver AAA");
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String s = objectMapper.writeValueAsString(errorResult);// 객체를 json으로 바꿔줄 때 사용

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(s);
                    // response에 먼가 조치를 취함으로써view를 응답할 필요가 없다보니 container에서 정상응답으로 쳐버린다 같은데.

                    return new ModelAndView(); // 예외를 정상흐름으로 리턴, 서블릿컨테이너에 새로 지정한 response로 전달됨.

                } else {
                    // Text/Html
                    log.info("UserException resolver BBB");
                    return new ModelAndView("error/500"); // view 지정 가능
                }


            }
        } catch (Exception e) {
            log.info("IOException, UserHandlerExceptionResolver 에서 발생 ");
        }

        return null;
    }
}
