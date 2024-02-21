package com.minod.itemservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;


@Slf4j
public class LogFilter implements Filter { //  jakarta.servlet.Filter; 이거다.
    /**
     * init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
     * doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
     * destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.
     */

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
        log.info("log filter doFilter"); // 요청이오면 doFilter부터 호출됨.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString(); // 사용자구분위해

        try {
          log.info("Request [{}][{}]", uuid, requestURI);
          chain.doFilter(request, response); // 핵심, 다음필터로 넘김, 다음필터 없으면 디스패처 서블릿이 호출됨. 이거없으면 그냥 끝이남.

        } catch (Exception e) {
            throw e;
        } finally {
            log.info("Response [{}][{}]", uuid, requestURI);
        }
    }
}
