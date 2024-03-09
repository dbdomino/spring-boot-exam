package com.minod.proxy.리플랙션동적프록시;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    @Test
    void reflection0() {
        Hello target = new Hello(); // 공통로직 담당 클래스

        log.info("start");
        String result1 = target.callA();
        log.info("result={}", result1);

        log.info("start");
        String result2 = target.callB();
        log.info("result={}", result2);
    }

    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // aClass는 클래스 메타정보를 획득하기 위해 사용. 내부클래스일 경우, $클래스명 쓴다.
        Class aClass = Class.forName("com.minod.proxy.리플랙션동적프록시.ReflectionTest$Hello");
        Hello target = new Hello();

        // 객체 생성 후, 객체의 메서드를 메타정보("callA")로 가져와 메서드객체 생성, 메서드 객체에 invoke로 메서드 실행

        // aClass의 메서드 callA 정보를 가져옴. 메서드 이름으로 가져옴. 익셉션 겨서 throws에 선언.
        Method methodCallA = aClass.getMethod("callA");
        // invoke를 통해 methodCallA를 동적으로 실행, 익셉션 생겨서 throws에 선언.
        // 여기서 methodCallA 는 Hello 클래스의 callA() 이라는 메서드 메타정보이다. methodCallA.invoke(인스턴스) 를 호출하면서
        // 인스턴스를 넘겨주면 해당 인스턴스의 callA() 메서드를 찾아서 실행한다. 여기서는
        //target 의 callA() 메서드를 호출한다.
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", "result1");

        // aClass의 메서드 callB 정보를 가져옴
        Method methodCallB = aClass.getMethod("callB");
        Object result2 = methodCallB.invoke(target); // 클래스의 메타정보로 메서드를 지정해 invoke로 호출,
        // 메서드 객체만 다르지 실행하는 invoke라는 명령은 같다... 추상화라고하네 이걸?
        log.info("result2 = {}", result2);

        /** 여기서 핵심은? 메서드를 동적으로 가져올 수 있다.
         *  callA(), callB()라는 메서드를 Method 형식인 methodCallA, methodCallB로 대체해서 가져오는 것인데...
         *  왜 이것만 봐서 효율적이다 효과적이다 라는게 안 느껴질까?? 어짜피 if 로 돌릴 거 같은데
        */


    }

    @Test
    void reflection2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // aClass는 클래스 메타정보를 획득하기 위해 사용. 내부클래스일 경우, $클래스명 쓴다.
        Class aClass = Class.forName("com.minod.proxy.리플랙션동적프록시.ReflectionTest$Hello");
        Hello target = new Hello();

        Method methodCallA = aClass.getMethod("callA");
        dynamicCall(methodCallA, target); // 아래 invoke 대신 실행
//        Object result1 = methodCallA.invoke(target);

        Method methodCallB = aClass.getMethod("callB");
        dynamicCall(methodCallB, target); // 이건 반환값이 있는 메서드를 수행하더라도, 반환값이 버려지는 구조같은데?

    }

    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
//        String result1 = target.callA();  // 여기는 callA() 라고 코딩되어 있는 것이다. callB를 쓸 수가 없다.
        Object result1 = method.invoke(target); // 익셉션 추가, method객체에 따라 callA() 또는 callB() 수행가능. 대신 수행할 메서드를 외부에서 설정해줘야 함. 제네릭처럼.
        log.info("result={}", result1);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("헬로 로그A callA");
            return "A";
        }
        public String callB() {
            log.info("헬로 로그B callB");
            return "B";
        }
    }

}
