package com.minod.itemservice.controller.login;

import com.minod.itemservice.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        // 서버에 저장된 세션정보를 쿠키에 저장된 세션ID를 가지고 확인해봄.
        HttpSession session = request.getSession(false);
        StringBuffer sb = new StringBuffer();
        if (session == null) {
            return "세션 없음.";
        }

        // 세션은 이터레이터로뽑아 반복돌릴수있다.
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> {log.debug("session name={}, value={}", name, session.getAttribute(name));
                    sb.append("session name=").append(name).append(", value=").append(session.getAttribute(name)).append("\n");
                });
        log.info("sessionInfo session login_member : {}",session.getAttribute(SessionConst.LOGIN_MEMBER));

        log.info("sessionId={}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessTime={}", new Date(session.getLastAccessedTime()));
        log.info("inNew={}",session.isNew());

        sb.append("sessionId=").append(session.getId()).append("\n");
        sb.append("getMaxInactiveInterval=").append(session.getMaxInactiveInterval()).append("\n");
        sb.append("creationTime=").append(session.getCreationTime()).append("\n");
        sb.append("lastAccessTime=").append(session.getLastAccessedTime()).append("\n");
        sb.append("inNew=").append(session.isNew()).append("\n");
        /**
         * sessionId : 세션Id, JSESSIONID 의 값이다. 예) 34B14F008AA3527C9F8ED620EFD7A4E1
         * maxInactiveInterval : 세션의 유효 시간, 예) 1800초, (30분)
         * creationTime : 세션 생성일시
         * lastAccessedTime : 세션과 연결된 사용자가 최근에 서버에 접근한 시간, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청한 경우에 갱신된다.
         * isNew : 새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 sessionId ( JSESSIONID )를 요청해서 조회된 세션인지 여부
         */

        return sb.toString();
    }


}
