package com.minod.servlet.web.springmvc.v2;

import com.minod.servlet.domain.Member;
import com.minod.servlet.domain.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newform() {
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
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

//    @RequestMapping("/")   //아래와 다른역할이다. 슬러시 있고없고 구분하기
    @RequestMapping
    public ModelAndView members(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

//        ModelView modelView = new ModelView("members");
        ModelAndView modelView = new ModelAndView("members");
        modelView.getModel().put("members",members);

        return modelView;
    }

//    @RequestMapping("/springmvc/v2/members/new-form")
//    public ModelAndView newform() {
//        return new ModelAndView("new-form");
//    }
//
//    @RequestMapping("/springmvc/v2/members/save")
//    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
////        String username = paramMap.get("username");
////        int age = Integer.parseInt(paramMap.get("age"));
//        String username = request.getParameter("username");
//        int age = Integer.parseInt(request.getParameter("age"));
//
//        Member member = new Member(username, age);
//        memberRepository.save(member);
//
////        ModelView modelView = new ModelView("save-result"); // 직접만들었던것
//        ModelAndView modelView = new ModelAndView("save-result");
//        modelView.getModel().put("member",member);
//        modelView.addObject("member",member);
//
//        return modelView;
//    }
//
//    @RequestMapping("/springmvc/v2/members")
//    public ModelAndView members(Map<String, String> paramMap) {
//        List<Member> members = memberRepository.findAll();
//
////        ModelView modelView = new ModelView("members");
//        ModelAndView modelView = new ModelAndView("members");
//        modelView.getModel().put("members",members);
//
//        return modelView;
//    }
}
