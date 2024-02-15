package com.minod.servlet.web.springmvc.before;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller; // 이거잘지켜야됨 옛날꺼 쓰려면

@Component("/springmvc/old-controller")
public class OldController implements Controller {
    /*
    이 컨트롤러가 호출되기위해 필요한 것 2가지
    - HandlerMapping(핸들러 매핑)
        핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 하며, 이를 위해
        @Component나 Bean등록 필요
          @Component("네임등록") 으로 해결
    - HandlerAdapter(핸들러 어뎁터)
        핸들러 매핑으로 찾은 핸들러를 실행할 수 있는 핸들러 어뎁터가 필요하며, 이를 위해
        @Controller 인터페이스를 실행할 수 있는 핸들러 어뎁터를 찾고 실행해야함.
          implements Controller 로 해결
     */

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
//        return null;
        return new ModelAndView("new-form"); // 모델뷰에선 논리적이름만., viewResolver에서 물리적인 전체이름 나오도록해야함.
        // viewResolver를 만들어줘야함. 없으면 경로를 제대로 못찾음.
        /* 방법1 스프링 부트기능으로 프로퍼티에 prefix와 suffix 등록하기
        spring.mvc.view.prefix=/WEB-INF/views/
        spring.mvc.view.suffix=.jsp
        방법2 스프링 부트 실행시 viewResolver 객체 Bean으로 등록해서 prefix와 suffix 등록하기
        @Bean
        viewResolver internalResourceViewResolver() {
            return new InternalResourceViewResolver("/WEB-INF/views/" , ".jsp");
        }

        방법3 절대경로로 ModelAndView에 등록하기 return new ModelAndView("/WEB-INF/views/new-form.jsp");
         */

    }
}
