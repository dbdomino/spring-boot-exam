package com.minod.servlet.web.springmvc.v3;

import com.minod.servlet.domain.Member;
import com.minod.servlet.domain.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // @RequestMapping은 Get으로오든, Post로 오든 신경쓰지 않는다.
    // 신경 쓰게하려면 별도로 수정해야한다.
//    @RequestMapping("/new-form")
    @RequestMapping(value="/new-form", method= RequestMethod.GET)
    @GetMapping("/new-form") // 위의 RequestMapping이랑 같은의미
    public String newform() {
        return "new-form"; // ModelAndView 를 반환해도 되고, String을 반환해도 된다. String을 반환한다면, 파일의 이름정보가 들어간다.
        // jsp 안붙여도되나?
    }

//    @RequestMapping("")
    @RequestMapping(value = "/save", method= RequestMethod.POST)
    @PostMapping("/save")// 위의 RequestMapping이랑 같은의미
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);

        return "save-result";
    }

//    @RequestMapping("/")   //아래와 다른역할이다. 슬러시 있고없고 구분하기
//    @RequestMapping   // @RequestMapping("") 과 같은 의미
    @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();

//        ModelAndView modelView = new ModelAndView("members");
        model.addAttribute("members",members); // model 에 담아놓으면 알아서 ModelAndView의 request로 전달함.

        return "members";
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
