package com.minod.itemservice.argumentResolver;

import com.minod.itemservice.repository.member.Member;
import com.minod.itemservice.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    // ArgumentResolver 인자를 엮어서 어노테이션이 사용가능해지도록 해줌. 어렵다아어려워.
    // @ModelAttribute 를 @Login으로 새로만들어 대체하기 위한 소스

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        // 파라미터들 지원하는지 보고 true/false 구분
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");
        // Argument resolver가 어떤 수행을 할 것 인가?
        // 세션에있는 값으로 member 뽑아낼 수 있으면 member객체로 파라미터 받아와서 바로사용하기 위함.
        // request 세션 null 이면 그냥 null 반환, 아니면, return을 해야함. 뭘? 세션에 걸쳐진 반환값을.
        // 세션에 addAttribute한 member를 반환함.
        // 그래서 return getAttribute(SessionConst.LOGIN_MEMBER);
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
