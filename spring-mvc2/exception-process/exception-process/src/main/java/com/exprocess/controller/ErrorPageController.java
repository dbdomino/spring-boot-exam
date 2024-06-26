package com.exprocess.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@Slf4j
@Controller
public class ErrorPageController {
    //RequestDispatcher 에 정의된 상수들
//    public static final String ERROR_EXCEPTION = "jakarta.servlet.RequestDispatcher.exception"; // 예외
//    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type"; //예외 타입
//    public static final String ERROR_MESSAGE = "jakarta.servlet.RequestDispatcher.message"; // 오류메시지
//    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri"; //  클라이언트 요청 URI
//    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name"; // 오류가 발생한 서블릿 이름
//    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code"; // HTTP 상태 코드

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("error-page/404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("error-page/500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value="/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api (HttpServletRequest request, HttpServletResponse response) {
        /**
         * Postman으로 테스트  http://localhost:8080/api/members/ex
         * {"memberId": "spring",
         * "name": "hello spring"}
         * HTTP Header에 Accept 가 application/json 인 것을 꼭 확인하자.
         */

        Map<String, Object> result = new HashMap<>();
//        Object ex = request.getAttribute(ERROR_EXCEPTION); // jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION); // 에러가 들어오므로 최상위 에러로 형변환
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());
        log.info("Api errorPage 500 hohoho");
        log.info("request.getAttribute(ERROR_EXCEPTION) : {}",request.getAttribute(ERROR_EXCEPTION));
        log.info("request.getAttribute(message) : {}", ex.getMessage()); // 이게문제인데

        Integer statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }

    private void printErrorInfo(HttpServletRequest request) {
//        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
//        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
//        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
//        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
//        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
//        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }
}
