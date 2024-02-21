package com.minod.itemservice.controller.login;

import com.minod.itemservice.domain.login.LoginForm;
import com.minod.itemservice.domain.login.LoginService;
import com.minod.itemservice.repository.member.Member;
import com.minod.itemservice.session.SessionConst;
import com.minod.itemservice.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않아요.");
            return "login/loginForm";
        }
        // 로그인 성공시 쿠키 만들어서 보내줌.
        // 1. 세션 쿠키(브라우저종료되면 사라지는 쿠키), 쿠키에 시간정보 주지 않으면 됨.
        Cookie coo = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(coo); // 쿠키는 response에 담아야됨.

        model.addAttribute("member",loginMember);
        redirectAttributes.addAttribute("statusLogin", true);
        return "loginHome";

    }

//    @PostMapping("/login") // Session 매니저로 로그인진행, 쿠키를 컨트롤러에서 안만듬.
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않아요.");
            return "login/loginForm";
        }
        // 로그인 성공시 쿠키 만들어서 보내줌.
        // 1. 세션매니저 수행
        sessionManager.createSession(loginMember, response);

        model.addAttribute("member",loginMember);
        redirectAttributes.addAttribute("statusLogin", true);
        return "redirect:/";

    }

//    @PostMapping("/login") // HttpSession 사용.
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않아요.");
            return "login/loginForm";
        }
        // 로그인 성공시 세션을 쿠키에 담아 보내주는게 getSession() 안에 다있다.
        // 1. HttpSession 을 통해 수행, 세션이 있으면 있는 세션 반환(기존로그인상태임), 없으면 신규 세션 생성
        /**
         * 이름이 getSession()으로 되어있는 이유는 기본적으로 request의 세션값을 가져와서 비교후 만들기때문에 만드는이름이 안들어간 것.
         * requset의 getSession()메서드는 서버에 생성된 세션이 있다면 세션을 반환하고, 없다면 세 세션을 생성하여 반환한다.
         * (인수가 default가 true)
         * HttpSession session = request.getSession(false);
         * requst의 getSession()메서드의 피라미터로 false를 전달하면 이미 생성된 세션이 있을 때 그 세션을 반환하고 없으면 null을 반환한다.
         */
        HttpSession session = request.getSession(); // 세션이 생성된건 was의 메모리에 저장
        // 2. 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); // 세션에 추가적으로 {고정값 : member객체}로 저장
        session.setMaxInactiveInterval(1800); //1800초 타임아웃 수명 설정 선택적으로 가능,
        // 웃긴게, session 객체는 request.getSession()메소드를 실행하여 세션 저장소와 연결된 세션객체를 뜻하는 걸로 보인다.
        // createSession 이런 이름이아니라 해깔린다.request가 없으면 세션을 못만들고 관리도 불가하다.
        // session은 상태성을 일시적으로 보관하기위해 세션저장소에 잠시 저장하는것이므로 어쩔수 없는 것으로 이해하자.

        model.addAttribute("member",loginMember);
        redirectAttributes.addAttribute("statusLogin", true);
        return "redirect:/";

    }

    @PostMapping("/login") // 서블릿 필터 적용 후 redirect 적용 (로그인 이전 화면으로 이동시켜주기)
    public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
        @RequestParam(defaultValue="/") String redirectURL) {
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않아요.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession(); // 세션이 생성된건 was의 메모리에 저장된다.

        log.info("session before login_member : {}",loginMember.toString());
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); // 지금은 {uuid : member객체} 로 로그인구현되는게 아니라 {고정값 : member객체}로 저장
        session.setMaxInactiveInterval(60);
        log.info("session login_member : {}",session.getAttribute(SessionConst.LOGIN_MEMBER));

        model.addAttribute("member",loginMember);
        redirectAttributes.addAttribute("statusLogin", true);
//        return "redirect:/";
        return "redirect:"+redirectURL;

    }

//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 로그아웃,
        // 1. 세션 쿠키(브라우저종료되면 사라지는 쿠키), 쿠키에 시간정보 주지 않으면 됨.
        expireCookie(response);
        return "redirect:/";

    }

//    @PostMapping("/logout") // 세션 매니저로 로그아웃
    public String logoutV2(HttpServletRequest request) {
        // 1. 세션 쿠키(브라우저종료되면 사라지는 쿠키), 쿠키에 시간정보 주지 않으면 됨.
        sessionManager.expire(request);
        return "redirect:/";

    }

    @PostMapping("/logout") // HttpSession으로 로그아웃
    public String logoutV3(HttpServletRequest request) {
        // 1. 세션 쿠키(브라우저종료되면 사라지는 쿠키), 쿠키에 시간정보 주지 않으면 됨.
        HttpSession session = request.getSession(); // 로그인때랑 같은소스
        if (session != null) session.invalidate(); // 세션삭제 (서버에서 세션 삭제를 의미함. 쿠키에는 남아있을수도 있음.)
        return "redirect:/";

    }

    private static void expireCookie(HttpServletResponse response) {
        Cookie coo = new Cookie("memberId", null); // 쿠키 memberId에 null 매겨버리기
        coo.setMaxAge(0); // 유효 시간0 으로 만료시키기
        response.addCookie(coo); // 쿠키는 response에 담아야됨.
    }

}
