package com.minod.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Controller
//@RequestMapping //스프링은 @Controller 이 있어야 스프링 컨트롤러로 인식 , @RequestMappin 만 가지고는 안됨.
//@ResponseBody
@RestController
public interface OrderControllerV1 {
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/nolog")
    String noLog();
}
