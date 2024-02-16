package com.minod.springmvc.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello") // view이름
                .addObject("data", "hello!");
        return mav; // 뷰 리졸버 수행
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        // String 반환하면, @ResponseBody 유무에 따라 나뉨
        // - @ResponseBody 없으면 뷰 리졸버가 실행되어, return 경로의 view를 찾고 렌더링 한다.
        // - @ResponseBody 있으면 , 뷰리졸버 없이 HTTP 메시지 바디에 직접 "response/hello"를 쏜다.
        model.addAttribute("data", "hello!!");
        return "response/hello";
    }

    // 있지만 권장하지 않는 방법   컨트롤러 경로 이름과, view의 논리적이름이 똑같으면, void일때 경로이름(RequestMapping 경로)과 같은
    // view의 네임을 찾는다. (templates.response.hello.html 을 찾아서 반환해줌)
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        // void를 반환하면 HttpServletResponse, OutputStream(Writer) 같은 HTTP메시지 바디를 처리하는 파라미터가 없으면
        // 요청 URL을 참고해서 논리적으로 뷰 이름을 사용한다.
        model.addAttribute("data", "hello!!22");
    }
}
