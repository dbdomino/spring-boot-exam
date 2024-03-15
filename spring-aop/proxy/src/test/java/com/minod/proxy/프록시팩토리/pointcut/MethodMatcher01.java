package com.minod.proxy.프록시팩토리.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodMatcher;

import java.lang.reflect.Method;
// Pointcut직접 구현시 메서드 매치되는 역할
@Slf4j
public class MethodMatcher01 implements MethodMatcher {
    private String matchName = "save";

    @Override // 이거만 신경씀
    public boolean matches(Method method, Class<?> targetClass) {
        // 메서드 네임이
        boolean result = method.getName().equals(matchName);
        log.info("포인트컷 메서드 매처1 메서드 태그정보로 비교, 메서드가 save일 경우만 동작.");
        log.info("포인트컷 메서드 매처1 matches={}, targetClass={}",method.getName(), targetClass);
        log.info("포인트컷 result = {}", result);
        return result;
    }

    @Override // isRuntime이 false이면, 위의 matches()가 호출됨.  ture이면 아래의 matches()가 호출됨.
    // isRuntime()이 false이면 클래스의 정적 정보(클래스명, 메서드명)만 사용하므로 내부 캐싱을 통해 성능 향상 가능하나,
    // inRuntime()이 true이면 매개변수를 사용(args...)하기에 동적으로 변하는 매개변수를 지원할수 있는 pointcut을 구현 가능하나, 캐싱이용 안함.
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }
}
