package com.minod.serverstart.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

// ServletContainerInitializer  서블릿 컨테이너 초기화 인터페이스
// 애플리케이션 AppInit(내가 맘데로 만든 인터페이스) 을 초기화해보자.
// 그러려면 서블릿 컨테이너를 만든 사람들의 규칙을 따라야 한다.

// 중요, 수동으로 서블릿 컨테이너(WAS)를 초기화 하고 디스패처 서블릿 만들어 스프링 컨테이너와 연결
@HandlesTypes(AppInit.class) // 이게 핵심
public class ContainerInitializeV2 implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        // 핵심! @HandlesTypes(AppInit.class) 에 선언한 AppInit.class(인터페이스)의 구현체들이
        // set에 딸려온다고 함. 딸려온 것으로 로직을 만들어 서블릿 등록 해야함.
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("MyContainerInitV2 set = " + set);
        System.out.println("MyContainerInitV2 servletContext = " + servletContext);
        // 여기서부턴 서블릿 컨테이너 만든 사람들의 규칙을 따라야 한다.
        for (Class<?> appInitClass : set) { // appInitClass = 구현체
            try {
                // for 돌면서 appInit 구현체들이 set에 딸려온걸 하나씩 반복해서 서블릿 컨텍스트에 넣는다.\
                // 그러면 AppInit 구현체들도 로그로찍히는게 확인된다.
                AppInit appInit = (AppInit) appInitClass.getDeclaredConstructor().newInstance();
                appInit.onStartup(servletContext);

            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
