package com.exprocess.exception.advice;


import com.exprocess.exception.UserException;
import com.exprocess.exceptionhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    // 이 컨트롤러 안에서 IllegalArgumentException 이 터지면 여기 @ExceptionHandler에서 잡는다. 그리고 illegalExHandler의 메소드가 수행된다.
    // return으로 ErrorResult가 json으로 반환된다.
    // 근데 @ExceptionHandler 결과는 200 HTTP 상태코드다. throw로 에러 던진거 잡은거니까 200으로 주는게 맞긴한데, 그냥 400으로 잡아서 주고싶다면 변경가능
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 에러 상태코드만 바꿔 준다. 그러나 컨테이너에는 성공으로 응답하여 다시 호출하여 에러페이지 조회 안한다.
    @ResponseBody // 응답 값을 객체로 반환해서 바로 json으로 뿌릴라면 이게있던지, 아니면 위에 컨트롤러가 RestController이던지 있어야한다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("exceptionHandle  ex" );
        log.error("ExControllerAdvice illegalExHandler [IllegalArgumentException] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("ExControllerAdvice userExHandle [UserException] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.TOO_EARLY); // 여기있는걸 더 우선으로잡아주네.
    }

    // 파라미터로 Exception e 를 선언한 이유? 위의 illegalExHandler(IllegalAr... e) 이나 userExHandle(userException e) 에서 처리 못했을 때
    // 즉 선언못한 예외들 아래 /api2/members/{id}  의 ex 같은 것들
    // 여기로 다 넘어와서 처리시키기 위해 파라미터를 Exception e 최상위 예외 객체로 잡음.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500 으로 상태코드 만들어 보내겠단소리, 다른걸로 변경 가능
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("ExControllerAdvice exHandle [Exception]  ex");
        return new ErrorResult("EX", "내부 오류");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500 으로 상태코드 만들어 보내겠단소리, 다른걸로 변경 가능
    @ExceptionHandler
    public ErrorResult exHandleRuntimeException(RuntimeException e) {
        log.error("[exceptionHandle] exHandleRuntimeException [RuntimeException] ex");
        return new ErrorResult("EX", "내부 오류 RuntimeException");
    }

    // 이런식으로 2개이상의 예외 클래스 처리가능하도록 선언도 가능
//    @ExceptionHandler({AException.class, BException.class})
    public String exfor(Exception e) {
        log.info("exception e", e);
        return "OK";
    }

    // view도 별도로 지정가능
//    @ExceptionHandler({CException.class, DException.class})
    public ModelAndView modelAndView(Exception e) {
        log.info("exception e", e);
        return new ModelAndView("/error-page/403");  // 원하는 view로 갈수있도록 설정도 가능
    }
}
