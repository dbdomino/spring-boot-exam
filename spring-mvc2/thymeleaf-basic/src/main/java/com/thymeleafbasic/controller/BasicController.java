package com.thymeleafbasic.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "hello 안녕~~");
        model.addAttribute("data2", "hello <b>안녕~</b>"); // html에 가면 <b>tag는 적용안된다. 문자그대로 나옴.
        return "basic/text-basic";
    }

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("userList", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
        session.setAttribute("sessionData", "세션데이터 입력1");

        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "basic/basic-objects";
    }
    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "안녕하세요 " + data;
        }
    }

    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    @GetMapping("link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "하세요!");
        return "basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "data입니다.");
        return "basic/operation";
    }

    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }
    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "basic/each";
    }

    @GetMapping("/condition")
    public String condition(Model model) {
        addUsers(model);
        return "basic/condition";
    }

    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("data", "model로 받은 데이터.");
        return "basic/comments";
    }

    @GetMapping("/block")
    public String block(Model model) {
        addUsers(model);
        model.addAttribute("data", "model로 받은 데이터.");
        return "basic/block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model, HttpServletResponse resp) {
        model.addAttribute("user", new User("user유저V1",33));
        addUsers(model);
        resp.setCharacterEncoding("utf-8"); // 한글깨짐 방지 인코딩 적용

        return "basic/javascript-inline";
    }


    static class User {
        private String username;
        private int age;

        User(String username, int age) {
            this.username = username;
            this.age = age;
        }

        public String getUsername() {
            return username;
        }

        public int getAge() {
            return age;
        }
    }

    private void addUsers(Model model) {
        List<User> list1 = new ArrayList<>();
        List<User> list2 = new ArrayList<>();
        list1.add(new User("user1A", 10));
        list1.add(new User("user1B", 20));
        list1.add(new User("user1C", 30));

        list2.add(new User("user2A", 21));
        list2.add(new User("user2B", 25));
        list2.add(new User("user2C", 29));
        model.addAttribute("users1", list1);
        model.addAttribute("users2", list2);
    }
}
