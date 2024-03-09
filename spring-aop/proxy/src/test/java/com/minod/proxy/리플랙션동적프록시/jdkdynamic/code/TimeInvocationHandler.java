package com.minod.proxy.리플랙션동적프록시.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** import java.lang.reflect.InvocationHandler;
 * JDK 동적 프록시가 제공하는 InvocationHandler를 구현해서
 * JDK 동적프록시 구현체를 만들어 사용해야 한다.
 * 동적 프록시 객체를 만들기 위해 (invoke를 제공해줌.)
 * Object result1 = method.invoke(target); 의 invoke 와 비슷한듯....
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {
    private final Object target; // 동적 프록시가 호출할 대상, 대상 객체(인스턴스)

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    // Object proxy? 이건 뭐냐??
    // Object[] args는 메서드 호출시 넘겨줄 인수이다.
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행, 상속받은 Invocation 핸들러 구현체에서....");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 실행, 걸린시간 = {}, 상속받은 Invocation 핸들러 구현체에서.... ",resultTime);
        return result;
    }
}
