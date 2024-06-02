package com.minod.serverstart.container;

import com.minod.serverstart.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/** 여기 컨테이너 초기화 하는 소스에서, 서블릿을 등록하는 것이다.
 * 서블릿을 프로그램 방식으로 등록하는 것이라고 하며, 서블릿으로 등록할 java파일을 등록하고 url 과 매핑함
 * http://localhost:8080/hello-servlet
 * 규칙이다.. 외우기보단 따라야한다.
 *
 * 스프링 컨테이너 연결을 위한 디스패쳐 서블릿 등록
 */
public class AppInitV2Servlet implements AppInit {
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV2Servlet.onStartup");

        //스프링 컨테이너 생성, 길다....
        // config 읽을 것을 등록, 컨피그 여러개 등록도 가능
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        // 디스패처 서블릿 생성, appcontext라는 스프링 컨테이너를 디스패처 서블릿에 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너(WAS)에 등록
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcherServlet);

        // /spring/*  처럼 디스패처 서블릿을 통과할 "요청 경로"를 설정함.
        // http://localhost:8080/spring/* 로 시작하게 됨.
        // HelloConfig가 여기서 잡히므로 http://localhost:8080/spring/hello-spring 이 실행가능해짐.
        servlet.addMapping("/spring/*");

    }
}