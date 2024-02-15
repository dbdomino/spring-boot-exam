package com.minod.servlet.web.frontcontroller.v3.controller;

import com.minod.servlet.domain.Member;
import com.minod.servlet.domain.MemberRepository;
import com.minod.servlet.web.frontcontroller.ModelView;
import com.minod.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 { // shift + F6   -> 파일이름바꾸기
    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

        ModelView modelView = new ModelView("members");
        modelView.getModel().put("members",members);

        return modelView;
    }
}
