package com.dbjdbc.exception;

// 사용자 에러를 상속받은 커스텀에러 만듬. (DB관련 에러라고 알 수 있도록 extends 상위계층을 통해 묶음)
// 이름처럼, 데이터 중복일 경우 발생하는 에러로 사용하자.
// 커스텀 에러는 JDBC나 JPA같은 기술에 의존되지 않는다. 기술 변경이 있더라도 예외는 그대로 사용가능.
public class MyDuplicateKeyException extends MyDbException{
    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
