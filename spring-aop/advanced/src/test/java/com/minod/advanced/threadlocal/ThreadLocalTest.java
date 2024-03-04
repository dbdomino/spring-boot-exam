package com.minod.advanced.threadlocal;

import com.minod.advanced.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

// 로그 추적기, 동시성 문제 발생 확인, 멀티스레드시 제대로 값이 보장안됨.

@Slf4j
public class ThreadLocalTest {

    private ThreadLocalService fieldService = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");
        // 멀티스레드 만듬
        // 1. 멀티스레드로 수행할 거 구현
        Runnable userA = () -> {
            fieldService.logic("UserA");
        };
        /* 위는 이걸 줄인 것이다. 람다식으로
        Runnable userA = new Runnable() {
            @Override
            public void run() {
                fieldService.logic("UserA");
            }
        }
         */
        Runnable userB = () -> {
            fieldService.logic("UserB");
        };

        // 2. 멀티스레드 생성
        Thread threadA = new Thread(userA);
        Thread threadB = new Thread(userB);
        Thread threadC = new Thread();



        // 3. 멀티스레드 실행
        threadA.start();
//        sleep(2000); // 동시성문제 발생 x
        sleep(100); //동시성 문제 발생O
        threadB.start();
        sleep(2000);

    }
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 에러는 예외처리
        }
    }
}
