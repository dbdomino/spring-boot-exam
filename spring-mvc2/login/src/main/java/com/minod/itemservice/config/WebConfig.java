package com.minod.itemservice.config;

import com.minod.itemservice.Interceptor.LogInterceptor;
import com.minod.itemservice.Interceptor.LoginCheckInterceptor;
import com.minod.itemservice.argumentResolver.LoginMemberArgumentResolver;
import com.minod.itemservice.filter.LogFilter;
import com.minod.itemservice.filter.LoginCheckFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
//public class WebConfig {  // 서블릿 필터는 이렇게 써도된다.
public class WebConfig implements WebMvcConfigurer {  // 스프링 인터셉터 쓰려면 implements WebMvcConfigurer   써야한다. 난 이게 싫어

    // 인터셉터 추가하려면 스프링에서 제공하는 양식을 그대로 따라야만 한다. = 이해하는게 좋지만, 이해하기 싫으면 외워라.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // 인터셉터 등록
                .order(1) // 인터셉터의 호출 순서를 지정
                .addPathPatterns("/**") // 인터셉터를 적용할 URL 패턴
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 이 경로는 인터셉터를 쓰지 않는다는 소리.

        // 인터셉터 추가가 필터보다 편하긴함.
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"
                );
        /** 경로정보
         * ? 한 문자 일치
         * * 경로(/) 안에서 0개 이상의 문자 일치
         * ** 경로 끝까지 0개 이상의 경로(/) 일치
         * {spring} 경로(/)와 일치하고 spring이라는 변수로 캡처
         * {spring:[a-z]+} matches the regexp [a-z]+ as a path variable named "spring"
         * {spring:[a-z]+} regexp [a-z]+ 와 일치하고, "spring" 경로 변수로 캡처
         * {*spring} 경로가 끝날 때 까지 0개 이상의 경로(/)와 일치하고 spring이라는 변수로 캡처
         *
         * /pages/t?st.html — matches /pages/test.html, /pages/tXst.html but not /pages/toast.html
         * /resources/*.png — matches all .png files in the resources directory
         * /resources/** — matches all files underneath the /resources/ path, including /resources/image.png and /resources/css/spring.css
         * /resources/{*path} — matches all files underneath the /resources/ path and captures their relative path in a variable named "path";
         * /resources/image.png will match with "path" → "/image.png", and /resources/css/spring.css will match with "path" → "/css/spring.css"
         * /resources/{filename:\\w+}.dat will match /resources/spring.dat and assign the value "spring" to the filename variable
         */
    }

//    @Bean
    public FilterRegistrationBean logFilter() { // 필터를 쓰려면 FilterRegistrationBean 을 써서 @Bean으로 등록해야 한다고 함. 수동 Bean 등록
        // LogFilter는 직접만든 필터 구현체, FilterRegistrationBean 필터 보관객체 인듯한데
        // @ServletComponentScan @WebFilter(filterName = "logFilter", urlPatterns =/"/*") 로 필터 등록이 가능하지만 필터 순서 조절이 안된다. 따라서 FilterRegistrationBean 을 사용하자.
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);   // 필터 체인순서
        filterRegistrationBean.addUrlPatterns("/*"); // 필터범위 -> 이건 request 범위를 말하는거같음.

        return filterRegistrationBean;
    }

//    @Bean  // 필터 2개만들려면 이런식으로 FilterRegistrationBean을 하나 더 반환시켜야 한다.
    public FilterRegistrationBean logFilter2() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);   // 필터 체인순서
        filterRegistrationBean.addUrlPatterns("/*"); // 내부의 화이트리스트에서 걸러진다.
//        filterRegistrationBean.addUrlPatterns("/items"); // 하나의 필터에 여러 경로범위를 지정 가능하네
//        filterRegistrationBean.addUrlPatterns("/items/*"); // 필터범위

        return filterRegistrationBean;
    }

    @Override // 이걸 추가해줘야 등록이된다, Bean으로 등록하는게 아닌 Spring의 ArgumentResolver에 커스텀 ArgumentResolver를 추가해서, 어노테이션 기능을 추가한다.
    // 단순히 컨트롤러에 소스를 줄이기 위해서 쓴다. 개발자가 직접 어노테이션 맨들어서 편하게 사용하기 위해서라고 함.
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
