package com.minod.proxy.프록시패턴예시.concreteproxy.code;

// 구체 클래스 (인터페이스 없이 만들어진 클래스 라고 함.)
public class ConcreteClient {
    private ConcreteLogic concreteLogic;
    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute() {
        concreteLogic.operation();
    }

}
