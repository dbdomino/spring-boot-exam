package com.minod.serverstart.container;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

// 스프링에서 제공하는 서블릿 컨테이너 초기화 방법

/* 지금까지 ServletContainerInitializer 인터페이스를 구현해서 서블릿 컨테이너 초기화 코드를 만들었다.
ContainerInitializeV1
ContainerInitializeV2
    여기에 애플리케이션 초기화를 만들기 위해 @HandlesTypes 애노테이션을 적용했다.
    /META-INF/services/jakarta.servlet.ServletContainerInitializer 파일에 서블릿 컨테이너 초기화 클래스 경로를 등록했다.
이처럼 서블릿 컨테이너 초기화 과정은 상당히 번거롭고 반복되는 작업이다.
스프링 MVC는 이러한 서블릿 컨테이너 초기화 작업을 이미 만들어두었다.
서블릿 컨테이너 초기화 과정은 생략하고, 애플리케이션 초기화 코드만 작성하면 된다.
 */

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
