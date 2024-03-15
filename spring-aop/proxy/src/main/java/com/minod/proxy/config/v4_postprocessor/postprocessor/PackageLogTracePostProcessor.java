package com.minod.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/** 후 처리기를 통해 proxyfactory를 만들고, 프록시 객체 생성하기
 *
 */

@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
    // 아래 2개 외부에서 주입시킬 것이다. 외부라면 후처리기를 빈으로 등록하는 곳이겠지.
    private final String basePackage; // 특정 패키지에만 적용하기 위해
    private final Advisor advisor; // 프록시 팩토리에 넣을 advisor

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    // 파라미터의 bean과 beanName은 후처리기를 거치는 Bean을 말한다.
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //Bean으로 등록된 것을 프록시 객체로 바꿔서 Bean으로 등록하기 위해 After 구현
        // 보면 이 후처리기는 Bean으로 등록된 Bean을 모두 한번씩 필터링 하는 것으로 보인다.
        // 후처리기 특징으로 보이며, 패키지 명에 따라 프록시로 만들지 말지 정하는걸 해주지 않으면 문제가 생길 것으로 보인다.
        log.info("param beanName={} bean={}", beanName, bean.getClass());
        // 프록시 대상여부 체크? 대상여부아니면(지정된 패키지에 해당하지 않으면) 원본을 그대로 진행
        String packageName = bean.getClass().getPackageName(); // 패키지명 가져오기.
        if (!packageName.startsWith(basePackage)) { // 해당패키지에 속하지 않은것은 조작없음.
            return bean;
        }

        // 프록시 대상이면 프록시 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.setProxyTargetClass(true); // 반드시 CGLIB 로 만듬, 인터페이스 기반 동적프록시 대상일지라도
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy(); // 지금 여기보면 인터페이스 형변환 없이 그대로 프록시(프록시 객체)를 만들어 반환
        // 컴포넌트 스캔으로 인터페이스 기반이 아닌 것을 proxyfactory를 거치니 jdk.proxy2.$Proxy88 이런식으로 되어버림. 왜그럴까?
        // 프록시 팩토리의 기능오류로 보인다. 인터페이스 기반 Bean인지 아닌지 구분이 어려운 것 같다.
        log.info("create proxy : target={}, proxy={}", bean.getClass(), proxy.getClass());
        return proxy;

//        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
