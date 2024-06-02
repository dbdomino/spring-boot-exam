package com.minod.serverstart.container;


import com.minod.serverstart.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * http://localhost:8080/hello-spring
 * 보면, ContainerInitializeV3를 구체화시키지 않고, 인터페이스를 단 것 만으로 사용할 디스패처 서블릿을 초기화 할 수 있따.
 *
 * 스프링 MVC 제공 WebApplicationInitializer 활용
 * spring-web
 * META-INF/services/jakarta.servlet.ServletContainerInitializer
 * org.springframework.web.SpringServletContainerInitializer
 */
public class AppInitV3SpringMVC implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup 스프링 에서 제공하는 서블릿 컨텍스트 초기화 수행");

        // 스프링 컨테이너 생성 (AppInitV2와 같은 스프링 컨피그파일을 중복으로 등록 가능하다.)
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 스프링에서 제공하는 디스패처 서블릿 생성, 스프링 컨테이너와 디스패처 서블릿 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너(WAS)에 등록, 디스패어 서블릿 이름 확인중요(다른 디스패처 서블릿과 중복안됨)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcherServlet);

        // 모든 요청이 디스패처 서블릿을 통하도록
        // "경로 매핑 충돌은 더 상세한 것이 우선시된다."
        // 현재 등록된 서블릿 다음과 같다.
        //   / = dispatcherV3
        //   /spring/* = dispatcherV2
        //   /hello-servlet = helloServlet
        //   /test = TestServlet
        // 이런 경우 우선순위는 더 구체적인 것이 먼저 실행된다.

        servlet.addMapping("/");


    }
}
