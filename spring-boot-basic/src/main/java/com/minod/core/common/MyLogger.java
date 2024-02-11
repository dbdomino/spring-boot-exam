package com.minod.core.common;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
//@Scope(value = "request") // 스코프방식, ObjectProvice 사용했어야함. 주입시
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // 프록시 방식, ScopedProxyMode는 Enum, 그럼 주입시 ObjectProvider 안써도 동작된다.
/*
여기가 핵심이다. proxyMode = ScopedProxyMode.TARGET_CLASS 를 추가해주자. -> 가짜 프록시 클래스를 만들어서 주입시켜준다는데? Provider 주입처럼
적용 대상이 인터페이스가 아닌 클래스면 TARGET_CLASS 를 선택
적용 대상이 인터페이스면 INTERFACES 를 선택

가짜 프록시 객체는 실제 request scope와는 관계가 없다. 그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤 처럼 동작한다.
프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수 있다.
사실 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다. (다형성이랑 DI컨테이너가 가진 가장 큰 강점)
프록시는 꼭 웹스코프가아니라 다른곳에서도 사용가능하다.(AOP) -> 클라이언트 코드를 고치지않고 기능추가가능
단일책임원칙 SRP (하나의 클래스는 하나의 책임만 맡아야한다.)
개방폐쇄원칙 OCP (클래스는 확장에 용이하나 변경은 폐쇄해야한다
리스코프 치환원칙 LSP( 약속된 동작은 반드시 수행되어야 한다.
인터페이스 분리원칙 ISP( 동작을 위한 인터페이스로 분리하여 설계한다.
의존관계 역전 원칙 DIP( 의존관계를 추상화에 의존해야지, 구체화에 의존하면 안된다.
 */
public class MyLogger {
    private String uuid;
    private String requestURL;  // url정보 넣고 사용하기위한 controller 필요
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String Message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + Message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();   // 이거 거의 겹칠일 없는 randomUUID라고 한다. 생성시점에 uuid를 넣어준다.
        System.out.println("[" + uuid + "] request scope bean create: " + this);

    }

    @PreDestroy
    public void destroy() {
        System.out.println("[" + uuid + "] request scope bean destroy: " + this);
    }
}
