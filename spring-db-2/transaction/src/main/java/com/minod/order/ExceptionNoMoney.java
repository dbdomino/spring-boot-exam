package com.minod.order;

public class ExceptionNoMoney extends Exception { // 비즈니스 예외, 롤백하면 안되기에 체크드 예외로 만듬.
    public ExceptionNoMoney() {
        super();
    }

    public ExceptionNoMoney(String message) {
        super(message);
    }
}
