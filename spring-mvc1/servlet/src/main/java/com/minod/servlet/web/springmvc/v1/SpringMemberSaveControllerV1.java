package com.minod.servlet.web.springmvc.v1;

import com.minod.servlet.domain.Member;
import com.minod.servlet.domain.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringMemberSaveControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
//        String username = paramMap.get("username");
//        int age = Integer.parseInt(paramMap.get("age"));
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

//        ModelView modelView = new ModelView("save-result"); // 직접만들었던것
        ModelAndView modelView = new ModelAndView("save-result");
        modelView.getModel().put("member",member);
        modelView.addObject("member",member);

        return modelView;
    }
}
