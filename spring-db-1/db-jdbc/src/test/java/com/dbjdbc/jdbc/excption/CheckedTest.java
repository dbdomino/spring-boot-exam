package com.dbjdbc.jdbc.excption;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedTest {
    @Test
    @DisplayName("catch 잡히는지 확인")
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }
    @Test
    @DisplayName("throw 되는지 확인")
    void checked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class); // 이건 익셉션 온거 비교하는 명령으로, 이 라인 끝나도 계속 수행 가능
        System.out.println("예외 던지기 처리완료");

    }
    /**
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }
    /**
    * Checked 예외는
    * 예외를 잡아서 처리하거나, 던지거나 둘중 하나를 필수로 선택해야 한다.
    */
    static class Service {
        Repository repository = new Repository();
        // try catch 예외를 잡아서 처리하는 코드
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }
        /**
         * 체크 예외를 밖으로 던지는 코드 (밖은 현재 메소드를 호출한 곳을 의미한다. 즉 현재실행중인 메소드스택이 끝이남.)
         * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언해야한다.
         * 언체크드예외는 throw new MyCheckedException("에러메시지"); 처럼 그냥 던지기 가능하다.
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
            System.out.println("예외 던진다면 이거안나와야됨");
        }
    }
    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
