package com.minod.advanced.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {
    private String nameStore;

    public String logic(String name){
        log.info("저장 name={} -> nameStore={}", name, nameStore); // 둘다 String
        nameStore = name;
         // sleep 그냥쓰면, 체크드 에러 throws해줘야되서 메소드로 분리시킴.
        sleep(1000);
        log.info("조회 nameStore={}",nameStore);

        return nameStore;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 에러는 예외처리
        }
    }
}
