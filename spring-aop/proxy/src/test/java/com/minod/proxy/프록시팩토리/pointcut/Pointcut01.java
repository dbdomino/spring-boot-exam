package com.minod.proxy.프록시팩토리.pointcut;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

public class Pointcut01 implements Pointcut {
    // 아래 2개 모두 준비해야함.
    @Override
    public ClassFilter getClassFilter() { // 클래스로 비교하는 것
        return ClassFilter.TRUE;// TRUE로 모든 클래스 가능하게 하던지, 특정클래스 매치하던지 해야함. null은 널포인트익셉션남.
    }

    @Override
    public MethodMatcher getMethodMatcher() { // 메소드로 비교하는 것
        return new MethodMatcher01(); // 직접 구현한 MethodMatcher 넣어줘야 한다.
    }
}
