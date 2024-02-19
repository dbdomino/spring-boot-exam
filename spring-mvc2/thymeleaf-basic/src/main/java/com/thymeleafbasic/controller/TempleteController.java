package com.thymeleafbasic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template")
public class TempleteController {

    @GetMapping("/fragment")
    public String template() {

        return "template/fragment/fragmentMain";

    }

    @GetMapping("/layout")
    public String layout() {
        return "template/layout/layoutMain";
//        return "template/layout/base";
    }

    @GetMapping("/layoutExtend")
    public String layoutExtend() {
        return "template/layoutExtend/layoutExtendMain";
    }
}
