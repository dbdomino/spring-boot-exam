package com.minod.core.controller;

import com.minod.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    // 의존관계 받을거임, 왜받아야함?? logic 에서 메소드 불러와 수행하려고
    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;

    public void logic(String id) {
//        MyLogger myLogger = myLoggerProvider.getObject(); // getObject()를 logDemo가 실행되는 시점으로 바꾼다.

        myLogger.log("service id = " + id);
    }
}
