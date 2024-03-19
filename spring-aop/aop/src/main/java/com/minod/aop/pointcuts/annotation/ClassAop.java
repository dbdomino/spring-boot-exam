package com.minod.aop.pointcuts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//AOP 어노테이션 필요한 것
@Target(ElementType.TYPE) // 클래스에다 붙이는 어노테이션이면 Type으로 써야 함.
@Retention(RetentionPolicy.RUNTIME) // 어노테이션이 런타임 실행되는동안 살아있는 것
public @interface ClassAop {

}
