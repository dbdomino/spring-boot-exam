package com.minod.itemservice.controller;

import com.minod.itemservice.argumentResolver.Login;
import com.minod.itemservice.repository.member.Member;
import com.minod.itemservice.repository.member.MemberRepository;
import com.minod.itemservice.session.SessionConst;
import com.minod.itemservice.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
//        return "redirect:/items";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required=false) Long memberId, Model model){
        // @CookieValue 딱 봐도 쿠키에 있는 key를 매핑시키는 기능
        if (memberId == null) return "home";

        // 로그인상태
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) return "home"; // 쿠키에 엄한값이 있네

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/") // 세션매니저로 값 가져오기
    public String homeLoginV2(HttpServletRequest request, Model model){
        // 세션매니저에 SessionID 있는지 확인
        Member member = (Member) sessionManager.getSession(request);

        // 비로그인상태
        if (member == null) return "home"; // 쿠키에 엄한값이 있네

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/") // HttpSession으로 세션 가져오기
    public String homeLoginV3(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);// getSession() 기본값이 true이므로 flase로 바꿔주자. 그래야 로그인안한 사용자는 안생긴다 세션

        // 비로그인
        if (session == null) return "home";

        // 로그인 시점에 세션에 보관한 회원 객체를 찾는다. 없으면 비로그인처리
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER); // loginMember 가져오깅??
        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/") // @SessionAttribute 으로 세션 가져오기
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        // @SessionAttribute   세션을 생성하는 기능이 아닙니다. 세션이 있으면 세션을 선언한 객체로 매핑시켜줍니다.
        // @SessionAttribute  기존에 session에 setAttribute 한게 있어야 이 기능이 가능합니다.
        log.warn("LoginComplete member={}",loginMember);
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) return "home";

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/") // ArgumentResolver로 세션 가져오기, 임의로 만든 @Login 어노테이션이 @ModelAttribute 처럼 동작하게 하려면 ArgumentResolver를 만들어줘야한다..
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) return "home";

        //세션이 유지되면 로그인 홈화면으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

}