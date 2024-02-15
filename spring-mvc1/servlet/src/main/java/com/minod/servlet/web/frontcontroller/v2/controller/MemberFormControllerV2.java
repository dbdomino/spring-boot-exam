package com.minod.servlet.web.frontcontroller.v2.controller;

import com.minod.servlet.domain.MemberRepository;
import com.minod.servlet.web.frontcontroller.MyView;
import com.minod.servlet.web.frontcontroller.v2.ControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class MemberFormControllerV2 implements ControllerV2 { // ctrl+w 로 범위지정 쉽게하네
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String viewPath = "/WEB-INF/views/new-form.jsp";
//        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
//        dispatcher.forward(request, response);
        System.out.println("MemberFormControllerV2.process");

        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
