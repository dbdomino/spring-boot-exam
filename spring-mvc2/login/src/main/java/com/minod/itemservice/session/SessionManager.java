package com.minod.itemservice.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 세션관리 Bean
@Component
public class SessionManager {
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    private static final String SESSION_COOKIE_NAME = "SessionId";

    /**
     * 세션 생성
     */
    public  void createSession(Object value, HttpServletResponse response) {
        // 세션 Id = UUID 생성
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 쿠키
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);

    }

    public Object getSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // 쿠키 request로 받을땐 배열로 들어옴
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return sessionStore.get(cookie.getValue()); // 쿠키에 SessionId 에 해당하는
            }
        }
        return null; // 쿠키에 SessionId 없으면 null로 반환
    }

    // getSession2 세션 가져오기 리펙터링
    public Object getSessionSimple(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) return null;
        return sessionStore.get(sessionCookie.getValue());
    }

    // findCookie 쿠키만 찾는 메서드
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies(); // 쿠키 request로 받을 땐 배열로 들어옴.
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null); // 안찾아지면 orElse로 디폴트값인 null 반환

    }

    /**
     * 세션 만료 시키기, UUID : value 없애버리면 됨.
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }
}
