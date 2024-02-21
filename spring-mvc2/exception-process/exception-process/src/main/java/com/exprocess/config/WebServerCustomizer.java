package com.exprocess.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
// WebServerFactoryCustomizer<ConfigurableWebServerFactory> 스프링 부트에서 제공하는 서블릿 팩토리...
// Bean으로 등록해서 사용함.
// 에러시 아래 지정된 경로로 다시 호출하는데, 이를 위해 오류페이지를 처리할 컨트롤러가 필요하다. ErrorPageController
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404= new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500= new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx= new ErrorPage(RuntimeException.class, "/error-page/500"); // 런타임도 걍 같은화면으로
        // 500 예외가 서버 내부에서 발생한 오류라는 뜻을 포함하고 있기 때문에 여기서는 예외가 발생한 경우(errorPageEx)도 500 오류 화면으로 처리했다.
        // 오류 페이지는 예외를 다룰 때 해당 예외와 그 자식 타입의 오류를 함께 처리한다. 예를 들어서 위의 경우 RuntimeException 은 물론이고 RuntimeException 의 자식도 함께 처리한다.

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx); // 핵심.
    }
}
