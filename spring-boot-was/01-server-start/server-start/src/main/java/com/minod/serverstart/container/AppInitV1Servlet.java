package com.minod.serverstart.container;

import com.minod.serverstart.servlet.HelloServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

/** 여기 컨테이너 초기화 하는 소스에서, 서블릿을 등록하는 것이다.
 * 서블릿을 프로그램 방식으로 등록하는 것이라고 하며, 서블릿으로 등록할 java파일을 등록하고 url 과 매핑함
 * http://localhost:8080/hello-servlet
 * 규칙이다.. 외우기보단 따라야한다.
 */
public class AppInitV1Servlet implements AppInit {
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV1Servlet.onStartup");
        //순수 서블릿 코드 등록
        ServletRegistration.Dynamic helloServlet =
                servletContext.addServlet("helloServlet", new HelloServlet());
        helloServlet.addMapping("/hello-servlet");
        // 스프링 처럼 @Configuration 으로 설정끌어와서하는거랑 @Component로 클래스마다 붙여 빈으로 등록하는것과 구조가 비슷하다.
    }
}