package com.minod.advanced.콜백패턴;

import lombok.extern.slf4j.Slf4j;

// 콜백패턴 쓰기위해 콜백으로 사용할 Stragegy,
// 그리고 콜백을 전달받고 실행할 객체인 Template 역할을 담당하는 Context 가 이거

@Slf4j
public class TemplateCallBack {
    public void execute(CallBackLogTracer callback) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        callback.call(); //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
