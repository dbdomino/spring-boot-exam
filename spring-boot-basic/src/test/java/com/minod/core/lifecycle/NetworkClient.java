package com.minod.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {   // InitializingBean, DisposableBean  이두개가 핵심, 콜백관련
    private String url;


    // 초기화, 소멸 인터페이스 단점
    // 이 인터페이스는 스프링 전용 인터페이스다. 해당 코드가 스프링 전용 인터페이스에 의존한다. InitializingBean, DisposableBean
    // 잘 사용하지 않는다고 한다.  implements InitializingBean, DisposableBean 빼고 커스텀 메소드로 구현해보자.


    /*@Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");

    }*/
    @PostConstruct // jakarta 어노테이션으로 스프링이없어도 지원가능,자바 표준, 다른 컨테이너에서도 동작 가능
    public void init() throws Exception {  // 메소드 이름도 내맘데로, 이후 Bean등록시에 어노테이션 옵션에 추가해줘야한다.
        connect();
        call("초기화 연결 메시지");

    }

    /*@Override
    public void destroy() throws Exception {
        disconnect();
        call("연결끊길때. destroy() 끝");
    }*/
    @PreDestroy
    public void close() throws Exception {
        disconnect();
        call("연결끊길때. destroy() 끝");
    }

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect() { // 서비스호출
        System.out.println("서비스 호출 connect()");

    }

    public void call(String message) {
        System.out.println("call : " + url + " , message = " + message);
    }

    public void disconnect() {
        System.out.println("close: " + url);
    }
}
