package com.exprocess.exception;

public class UserException extends RuntimeException {
    // 예외가 발생하면 WAS까지 예외가 던져지고, WAS에서 오류 페이지 정보를 찾아서 다시 /error 를 호출하는 과정은 생각해보면 너무 복잡하다.
    // ExceptionResolver 를 활용하면 예외가 발생했을 때 이런 복잡한 과정 없이 여기에서 문제를 깔끔하게 해결할 수 있다.
    // 즉 예외 발생했을 때 WAS에서 다시 /error호출 안하는 방법을 구현해본다.
    // 이를 위해 커스텀 에러를 만든다..

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
