package com.minod.servlet.web.springmvc.v1;

import com.minod.servlet.domain.Member;
import com.minod.servlet.domain.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class SpringMemberListControllerV1 {
    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

//        ModelView modelView = new ModelView("members");
        ModelAndView modelView = new ModelAndView("members");
        modelView.getModel().put("members",members);

        return modelView;
    }
}
