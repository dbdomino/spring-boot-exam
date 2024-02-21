package com.exprocess.config;

import com.exprocess.filter.LogFilter;
import com.exprocess.interceptor.LogInterceptor;
import com.exprocess.resolver.MyHandlerExceptionResolver;
import com.exprocess.resolver.UserHandlerExceptionResolver;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 인터셉터 쓰기위한 준비 implements WebMvcConfigurer

    // configureHandlerExceptionResolvers(..) 를 사용하면 스프링이 기본으로 등록하는
    // ExceptionResolver 가 제거되므로 주의, extendHandlerExceptionResolvers 를 사용하자
    @Override  // HandlerExceptionResolver 쓰는방법
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver()); // view가 지정된 리졸버
    }

    @Override // 인터셉터 쓰는방법
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**"); // /error-page/** 로 오류페이지 열 때 인터셉터 사용안함 설정.
                .excludePathPatterns("/css/**", "*.ico"); // /error-page/** 로 오류페이지 열 때 인터셉터 사용안함 설정.
        // DispatcherType은 인터셉터에서 지원안함.

    }

//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        // DispatcherType이 REQUEST 이거나 ERROR일 때 필터가 호출된다.
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 기본은 REQUEST만 있다.
        // 필터에 DispatcherType.ERROR 가 없다면, 에러일 경우 필터를 거치지 않는다.
        // 에러일때 필터를 거친다면, 거기서 뭔가 추가적인 정보를 에러 뷰에 뿌리는게 가능하도록 조치도 할 수 있을 것 같다.

        return filterRegistrationBean;
    }
}
