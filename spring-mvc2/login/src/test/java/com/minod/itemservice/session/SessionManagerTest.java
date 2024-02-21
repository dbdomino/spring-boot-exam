package com.minod.itemservice.session;

import com.minod.itemservice.repository.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {
        // 테스트를 위한 가짜 객체, Mock Response를 만듬.
        MockHttpServletResponse response = new MockHttpServletResponse();
        // 세션 생성
        Member member = new Member();
        sessionManager.createSession(member, response); // 브라우저로 response 응답이 나감.
        // 서버측 세션스토어에 UUID : Member객체 로 저장됨

        // 이후 새로운 요청에 쿠키가 들어있는지 확인하자.
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); // 요청에 이전 MockHttpServletResponse 로 나갔던 쿠키가 request에 있도록 함.

        // 세션조회 결과확인
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member); //result 는 request에 담긴 SessionValue로 member객체를 말함.
        // member는 이전에 response할때 만든 세션의 객체를 말함.

        // 세션만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request); // 세션 만료시켰다면 세션스토어에 세션 없어야됨.
        assertThat(expired).isNull();
    }
}
