package com.minod.serverstart.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

// ServletContainerInitializer  서블릿 컨테이너 초기화 인터페이스
// 서블릿 컨테이너는 실행 시점에 초기화 메서드인 onStartup() 을 호출해준다.
// 여기서 애플리케이션에 필요한 기능들을 초기화 하거나 등록할 수 있다.

// 아래 onStartup()을 구현하고 WAS에게 실행할 초기화 클래스를 알려줘야 초기화가 수행된다.(중요)
// resources/META-INF/services/jakarta.servlet.ServletContainerInitializer 파일을 해당 경로까지 만들어 생성해야함. (규칙임. 서블릿의)
public class ContainerInitializeV1 implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyContainerInitV1.onStartup");
        System.out.println("MyContainerInitV1 set = " + set);
        System.out.println("MyContainerInitV1 servletContext = " + servletContext);
    }
}
