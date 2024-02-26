package com.dbjdbc.jdbc.excption;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedAppTest {
    // 체크드 에러를 언체크드 에러로 바꿔 출력하는 방법 (커스텀 에러로 바꿔서 출력하기.)
    // 커스텀 에러 객체에서 생성자로 (Throwable e)로 받아 super(e)시키는게 핵심. (Throwable cause)
    // 언체크드 예외(대표적으로 런타임 애러)는 대부분 복구불가능한 예외로써, 여기서 해결 하는게 아니라, 던지고 공통로직으로 에러처리하기 위해 사용

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }
    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//e.printStackTrace();
//            log.info("ex", e); // 여기 출력시 SQLException정보가 포함된 RuntimeSQLException의 에러정보를 볼 수 있다.
//            log.info("ex"); // 여기 출력시 SQLException정보가 포함된 RuntimeSQLException의 에러정보를 볼 수 있다.
            log.info("ex {} hoho", e); // 여기 출력시 SQLException정보가 포함된 RuntimeSQLException의 에러정보를 볼 수 있다.
        }
    }

    static class Controller {
        Service service = new Service();
        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();
        public void logic() {
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() {
            try {
                runSQL(); // ex로 SQLException(JDBC 에러) throw 받음. -> catch로 가서 RuntimeSQLException(커스텀에러)으로 바꿔 에러출력
                // 즉 체크드 에러를 언체크드 에러로 바꿔 날림. 언체크드 에러에 기존에러정보를 포함시키려고
                // RuntimeSQLException 만들때 Throwable e를 super 시켜서 SQLException의 e 정보를 같이 넘김.
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }
        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }
    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {
        }
        public RuntimeSQLException(Throwable cause) { // Throwable 로 에러 생성자 만들면, 이전 예외를 포함해서 에러를 만들 수 있다.
            super(cause); // exception 객체 만든게 들어갈 수 있음.
        }
    }




}
