package com.minod.advanced.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {
//    private String nameStore;
    // 쓰레드 로컬 이라는 객체에, 제네릭 타입을 지정하고 제네릭대로 값을 저장할 수 있다.
    // 쓰레드 로컬 안에 저장된 값은, 쓰레드마다 저장을 보장한다.
    private ThreadLocal<String> nameStore = new ThreadLocal<>(); //값 저장을 쓰레드 로컬에 저장한다.

    public String logic(String name){
        log.info("저장 name={} -> nameStore={}", name, nameStore.get()); // 둘다 String
//        nameStore = name;
        nameStore.set(name);
         // sleep 그냥쓰면, 체크드 에러 throws해줘야되서 메소드로 분리시킴.
        sleep(1000);
        log.info("조회 nameStore={}",nameStore);
        log.info("조회 nameStore.get()={}",nameStore.get());

        return nameStore.get(); // 쓰레드 로컬에 저장된 값을 꺼낸다.
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 에러는 예외처리
        }
    }
}
