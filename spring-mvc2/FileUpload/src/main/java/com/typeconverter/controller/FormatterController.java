package com.typeconverter.controller;

import com.typeconverter.formatter.FormatterSample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class FormatterController {

    @ResponseBody
    @GetMapping("/formatter/test")
    public String formatterTest(Model model) {
        FormatterSample formatterSample = new FormatterSample();
        formatterSample.setNumber(10000000);
        formatterSample.setLocalDateTime(LocalDateTime.now());
        System.out.println(formatterSample.getNumber()); // 10000000 값은 포멧터 적용안된채로 나옴.
        System.out.println(formatterSample.getLocalDateTime());
        return "formatterSample.getNumber()";
    }

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        FormatterSample formatterSample = new FormatterSample();
        formatterSample.setNumber(10000000);
        formatterSample.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("formatter", formatterSample);
        return "formatter-form";
    }

    @PostMapping("/formatter/edit") // @ModelAttribute("form") 는 name 옵션 지정해준 이름으로 들어간다. 지정안해주면, 변수명보단 클래스명에서 첫글자 소문자로 바뀌어 name으로 들어간다.
    public String formatterEdit(@ModelAttribute(name="form") FormatterSample form2, Model model) {
//    public String formatterEdit(@RequestBody FormatterSample form, Model model) {

//        model.addAttribute("form", form);
        FormatterSample form1 = (FormatterSample) model.getAttribute("form");
        System.out.println(form1);
        log.info("form  get number: {}",form1.getNumber());

        return "formatter-view";
    }
}