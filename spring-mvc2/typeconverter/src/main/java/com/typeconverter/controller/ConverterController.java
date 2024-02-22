package com.typeconverter.controller;

import com.typeconverter.converter.IpPort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ConverterController {
    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.1.2",8088));
        return "converter-view";
    }

    @GetMapping("/converter/edit")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.1",8080);
        Form form = new Form(ipPort);

        model.addAttribute("form", form);
        return "converter-form";
    }
    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {
        IpPort ipPort = form.getIpPort();
        model.addAttribute("ipPort", ipPort);
        return "converter-view";
    }

    /**
     * GET /converter/edit
     * th:field 가 자동으로 컨버전 서비스를 적용해주어서${{ipPort}} 처럼 적용이 되었다. 따라서 IpPort String 으로 변환된다.
     * POST /converter/edit
     * @ModelAttribute 를 사용해서 String  IpPort 로 변환된다.
     */

    @Data
    static class Form {
        private IpPort ipPort;
        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }
}
