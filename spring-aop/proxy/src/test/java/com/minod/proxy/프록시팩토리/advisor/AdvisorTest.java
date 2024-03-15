package com.minod.proxy.프록시팩토리.advisor;

// Advisor Test
// 여기서 어드바이저는 TimeAdvice() 인스턴스 구현체(=Advice 객체)를 가지고 만들어야 한다.

import com.minod.proxy.cglib.ServiceImpl;
import com.minod.proxy.cglib.ServiceInterface;
import com.minod.proxy.프록시팩토리.advice.AdviceSample1;
import com.minod.proxy.프록시팩토리.advice.AdviceSample2;
import com.minod.proxy.프록시팩토리.advice.TimeAdvice;
import com.minod.proxy.프록시팩토리.pointcut.Pointcut01;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class AdvisorTest {

    /** Advosior는 ProxyFactory 인스턴스에 넣어서 쓰는 것이며, 별도 인스턴스로 만들어 넣어야 한다.
     * new DefaultPointcutAdvisor : Advisor 인터페이스의 가장 일반적인 구현체이다.
     * 생성자를 통해 하나의 포인트컷과 하나의 어드바이스를 넣어주면 된다.
     * 포인트컷은 대상 여부를 확인하는 필터 역할만 담당한다.(어디, 위치조건을 이야기하는 것 일수도 있다.)
     * 어드바이저는 포인트컷과 어드바이스를 둘다 가져야 하며, 프록시팩토리에 넣어 프록시 객체를 만드는데 사용된다.(dataSource 처럼)
     */
    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();// target은 아무거나

        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());// 포인트컷, Advice
        proxyFactory.addAdvisor(advisor); // 핵심.
        ServiceInterface proxyObject = (ServiceInterface) proxyFactory.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        proxyObject.save();
        proxyObject.find();
    }

    @Test
    /**
     * save() 에만 Advice 로직 적용하려면, 포인트컷 직접 구현 해야 함.
     */
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();// target은 아무거나
        Pointcut01 pointcut = new Pointcut01();// 포인트 컷 직접구현.

        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());// 포인트컷, Advice
        proxyFactory.addAdvisor(advisor); // 핵심.
        ServiceInterface proxyObject = (ServiceInterface) proxyFactory.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        proxyObject.save();
        proxyObject.find();
    }

    @Test
    /**
     * save() 에만 적용하도록, 포인트컷 구현이 아닌 메서드로 설정해서 사용하기
     */
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();// target은 아무거나
        Pointcut01 pointcut = new Pointcut01();// 포인트 컷 직접구현.
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut(); // 또다른 포인트 컷
        nameMatchMethodPointcut.setMappedNames("save","save2");// 포인트컷 구현한거 말고, 메서드로 만들어 사용가능.

        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(nameMatchMethodPointcut, new TimeAdvice());// 포인트컷, Advice
        proxyFactory.addAdvisor(advisor); // 핵심.
        ServiceInterface proxyObject = (ServiceInterface) proxyFactory.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        proxyObject.save();
        proxyObject.find();
    }

    @Test
    void multiAdvisorTest(){
        ServiceInterface target = new ServiceImpl();// target은 아무거나
        Pointcut01 pointcut = new Pointcut01();// 포인트 컷 직접구현.
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut(); // 또다른 포인트 컷
        nameMatchMethodPointcut.setMappedNames("save","save2");// 포인트컷 구현한거 말고, 메서드로 만들어 사용가능.

        ProxyFactory proxyFactory = new ProxyFactory(target); // 프록시팩토리 하나에 advisor2개 넣었다.

        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(pointcut, new AdviceSample1());// 포인트컷, Advice
        proxyFactory.addAdvisor(advisor1);  // AddAdvisor 넣은 순서대로 advisor 동작한다.
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(nameMatchMethodPointcut, new AdviceSample2());// 포인트컷, Advice
        proxyFactory.addAdvisor(advisor2);
        ServiceInterface proxyObject = (ServiceInterface) proxyFactory.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        proxyObject.save();
        System.out.println("---");
        proxyObject.find();
    }

    @Test
    void multiAdvisorTest2(){
        ServiceInterface target = new ServiceImpl();// target은 아무거나
        Pointcut01 pointcut = new Pointcut01();// 포인트 컷 직접구현.
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut(); // 또다른 포인트 컷
        nameMatchMethodPointcut.setMappedNames("save","save2");// 포인트컷 구현한거 말고, 메서드로 만들어 사용가능.

        ProxyFactory proxyFactory1 = new ProxyFactory(target); // 프록시팩토리 하나에 advisor2개 넣었다.
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(pointcut, new AdviceSample1());// 포인트컷, Advice
        proxyFactory1.addAdvisor(advisor1);
        ServiceInterface proxyObject1 = (ServiceInterface) proxyFactory1.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        ProxyFactory proxyFactory2 = new ProxyFactory(target); // 프록시팩토리 하나에 advisor2개 넣었다.
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(nameMatchMethodPointcut, new AdviceSample2());// 포인트컷, Advice
        proxyFactory2.addAdvisor(advisor2);
        ServiceInterface proxyObject2 = (ServiceInterface) proxyFactory2.getProxy(); // 프록시객체는 반환된걸 형변환해서 쓰더라.

        proxyObject1.save();
        System.out.println("---");
        proxyObject2.save();
    }
}
