package com.exprocess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/* 다음 두가지 경우 ResponseStatusExceptionResolver가 처리한다고 합니다.
@ResponseStatus 가 달려있는 예외
ResponseStatusException 예외

- 개발자가 변경할 수 없는 곳에는 적용이 어려운 exception이다.
- 다만 ExceptionHandler 별도 안구현해도 되고, config에도 안추가해도 되서 편하긴함.
- 다만 기능이 httpcode설정, reason 설정이 다인것 같다.

reason 익셉션으로 뜨게하려면 프로퍼티에 server.error.include-message=always 있어야되고, messages.properties에 reason이 있어야됩니다.
 */
// 커스텀 exception을 만들 때 reason을 설정하면 messages.properties 프로퍼티에서 알아서 찾아준다고 합니다.
@ResponseStatus(code = HttpStatus.PAYLOAD_TOO_LARGE, reason = "error.bad") // 적용됨
public class MessageException extends RuntimeException {
}
