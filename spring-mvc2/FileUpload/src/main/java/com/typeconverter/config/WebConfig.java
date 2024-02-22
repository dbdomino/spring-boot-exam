package com.typeconverter.config;

import com.typeconverter.converter.IpPortToStringConverter;
import com.typeconverter.converter.StringToIpPortConverter;
import com.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 스프링 컨버전서비스 등록하는 방법.
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
        /** 주의
         * StringToIntegerConverter , IntegerToStringConverter 를 꼭 주석처리 하자.
         * MyNumberFormatter 도 숫자 문자, 문자 숫자로 변경하기 때문에 둘의 기능이 겹친다.
         * 우선순위는 컨버터가 우선하므로 포맷터가 적용되지 않고, 컨버터가 적용된다.
         */
    }

}
