package com.minod.proxy.프록시패턴예시;

/**
 * Subject 인터페이스에 의존하고, Subject 를 호출하는 클라이언트 코드이다.
 * ProxyPatternClient -> Subject -> RealSubject 관계가 될 것이다.
 */
public class ProxyPatternClient {
    private Subject subject;

    public ProxyPatternClient(Subject subject) { // subject 구현체인 RealSubject가 주입될 것이다.
        this.subject = subject;
    }
    public void execute() {
        subject.operation();
    }
}
