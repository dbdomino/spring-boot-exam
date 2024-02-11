package com.minod.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.ComponentScan.*;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
) // excludeFilter 컴포넌트 스캔으로 등록할 빈 중에 뺄것을 지정함.
public class AutoAppConfig {
    // 기존의 AppConfig와는 다르게 @Bean으로 등록한 클래스가 하나도 없다.

    /*
    참고: 컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에,
AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 그래서
excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다. 보통 설정 정보를 컴포넌트 스캔
대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다.

컴포넌트 스캔관련 새롭게 테스트해보려고 config 중복으로 작성한건데, componentscan에서 중복스캔으로 실행안되는 오류 피하려고 excludeFilter쓴거
     */
/*   빈의 중복등록으로 충돌이 날 경우??
1. 자동 빈 등록 vs 자동 빈 등록 -> 오류발생 ConflictingBeanDefinitionException 예외 발생
2. 수동 빈 등록 vs 자동 빈 등록 -> 이 경우 수동 빈 등록이 우선권을 가진다.(수동 빈이 자동 빈을 오버라이딩 해버린다.) 수동 빈 =
 */

    // 수동빈 테스트
    // ComponentScan으로 읽기에 주석처리해줌 24.02.11
//    @Bean(name = "memoryMemberRepository")
//    public MemberRepository memberRepository() {
//        System.out.println("AutoAppConfig 두둥 AutoAppConfig.memberRepository");
//        return new MemoryMemberRepository();
//    }
    /*
    @Component    // 여기 클래스파일에도 컴포넌트 붙어있다.
    public class MemoryMemberRepository
     */


}