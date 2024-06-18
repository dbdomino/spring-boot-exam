package com.hello.controller;

import com.hello.domain.MelloServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//@Controller
//@RequestMapping(value = "/")

@ResponseBody
public class MelloController {
    @Autowired
    private MelloServe melloServe;

    @GetMapping("/mello")
    public String mello() {
        System.out.println("MelloController.mello()");
        return "mello mello~~";
    }
    @GetMapping("/mello-get")
    public String mello2() {
        melloServe.getMello();
        return "okok";
    }
}
