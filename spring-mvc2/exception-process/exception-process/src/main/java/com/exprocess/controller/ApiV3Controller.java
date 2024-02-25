package com.exprocess.controller;

import com.exprocess.exception.UserException;
import com.exprocess.exceptionhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 오류 내기 위한 컨트롤러 3
 * @ControllerAdvice
 * package com.exprocess.exception.advice;  의 ExControllerAdvice 의 @ExceptionHandler 사용테스트
 */
@Slf4j
@RestController
public class ApiV3Controller {

    @ResponseBody // 소스안에 일치하는 에러타입이 있으면 ControllerAdvice 보다 우선순위를 가져 반응한다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("exceptionHandle  ex" );
        log.error("ApiV3Controller illegalExHandler [IllegalArgumentException] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @GetMapping("/api3/members/{id}")
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
