package com.exprocess.controller;

import com.exprocess.exception.UserException;
import com.exprocess.exceptionhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 오류 내기 위한 컨트롤러 2
 * @ExceptionHandler
 *
 * 클래스 내에서 작동가능한 공통 exception 처리
 */
@Slf4j
//@RestController
public class ApiV2Controller {

    // 이 컨트롤러 안에서 IllegalArgumentException 이 터지면 여기 @ExceptionHandler에서 잡는다. 그리고 illegalExHandler의 메소드가 수행된다.
    // return으로 ErrorResult가 json으로 반환된다.
    // 근데 @ExceptionHandler 결과는 200 HTTP 상태코드다. throw로 에러 던진거 잡은거니까 200으로 주는게 맞긴한데, 그냥 400으로 잡아서 주고싶다면 변경가능
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 에러 상태코드만 바꿔 준다. 그러나 컨테이너에는 성공으로 응답하여 다시 호출하여 에러페이지 조회 안한다.
    @ResponseBody // 응답 값을 객체로 반환해서 바로 json으로 뿌릴라면 이게있던지, 아니면 위에 컨트롤러가 RestController이던지 있어야한다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("exceptionHandle  ex" );
        log.error("exceptionHandle [exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.TOO_EARLY); // 여기있는걸 더 우선으로잡아주네.
    }

    // 파라미터로 Exception e 를 선언한 이유? 위의 illegalExHandler(IllegalAr... e) 이나 userExHandle(userException e) 에서 처리 못했을 때
    // 즉 선언못한 예외들 아래 /api2/members/{id}  의 ex 같은 것들
    // 여기로 다 넘어와서 처리시키기 위해 파라미터를 Exception e 최상위 예외 객체로 잡음.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500 으로 상태코드 만들어 보내겠단소리, 다른걸로 변경 가능
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex");
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
