package com.dbjdbc.exception;

public class MyDbException extends RuntimeException{
    /** 사용자 Exception 만들기
     * 1. 사용자 Exception 파일 만들기
     * 2. extends로 타입 상속받기 (예를들어 RuntimeException)
     * 3. 생성자 자동생성하기 4개
     */

    public MyDbException() {
        super();
    }

    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(String message, Throwable cause) { // 이전 체크드 에러의 스텍 트래이스 정보가져오기위해
        super(message, cause);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }
}
