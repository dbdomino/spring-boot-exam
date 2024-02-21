package com.minod.itemservice.filter;

import com.minod.itemservice.session.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.util.UUID;


@Slf4j
public class LoginCheckFilter implements Filter { //  jakarta.servlet.Filter; 이거다.
    /**
     * init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
     * doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
     * destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.
     */

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LoginCheck filter doFilter"); // 요청이오면 doFilter부터 호출됨.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response; // 로그인체크위해 이것도 필요???

        String uuid = UUID.randomUUID().toString(); // 사용자구분위해

        try {
            log.info("isLoginCheck Request [{}][{}]", uuid, requestURI);
            if (isLoginCheckPath(requestURI)) { // whitelist 에 포함되는 경로라면 인증체크, if로직 안탐. 포함한되는 경로면 로직 탐.
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false); // 세션은 왜 불러옴? 세션저장소에 세션있는지 봐야된데.
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { // null이면 또는 로그인맴버 값도 없으면
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 응답으로 로그인으로 redirect, requestURI은 이전경로
                    httpResponse.sendRedirect("/login?redirectURL=" +requestURI);
                    return; // 반환값이 없는 void 메소드에서도 reutrn 문을 사용하면 바로 해당 메소드 스택을 빠져나갈 수 있습니다.

                }
            }
            // 실수로 이걸빼고 구현하는 경우도 있어서 문제될수 있다고함. 이거빼면 의미가없음
            chain.doFilter(request, response); // 핵심, 다음필터로 넘김, 다음필터 없으면 디스패처 서블릿이 호출됨. 이거없으면 그냥 끝이남.

        } catch (Exception e) {
            throw e;
        } finally {
            log.info("isLoginCheck Response [{}][{}]", uuid, requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
