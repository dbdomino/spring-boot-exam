package com.exprocess.controller;

import com.exprocess.exception.MessageException;
import com.exprocess.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * 오류 내기 위한 컨트롤러 1
 */

@Slf4j
@RestController
public class ApiController {
    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");// API 요청인데 HTML 페이지로 만들어둔 500페이지가 출력됨. API에선 json 으로에러가 나와야됨.
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 값");//  API 요청인데 HTML 페이지로 만들어둔 500페이지가 출력됨. API에선 json 으로에러가 나와야됨.
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");  // 사용자 에러 발생 시 컨테이너에서 다시 /error로 요청 안보내도록 처리하는 방법 구현
        }

        return new MemberDto(id, "hello " + id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new MessageException(); // 사용자 에러 발생
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        // 상태코드, 에러메시지를 한번에 붙일 수 있는 에러도 있다. ResponseStatusException , reason 등록으로 메시지 관리 가능
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad");
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        /**
         * DefaultHandlerExceptionResolver 는 스프링 내부에서 발생하는 스프링 예외를 해결한다.
         * 대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생하는데,
         * 이 경우 예외가 발생했기 때문에 그냥 두면 서블릿 컨테이너까지 오류가 올라가고, 결과적으로 500 오류가 발생한다. (BasicErrorController)
         * 그런데 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제이다. HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.
         * DefaultHandlerExceptionResolver 는 이것을 500 오류가 아니라 HTTP 상태 코드 400 오류로 변경한다. 스프링 내부 오류를 어떻게 처리할지 수 많은 내용이 정의되어 있다.
         *
         * TypeMismatchException 는 500에러인데, 스프링에서 잘못들어온 값에 문제가 있을 경우 400 오류로 변경해서 뿌려준다.
         * 스프링 내부 예외 처리 DefaultHandlerExceptionResolver
         */
        return "ok";
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
