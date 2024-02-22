package com.typeconverter.formatter;

import com.typeconverter.converter.IpPort;
import com.typeconverter.converter.IpPortToStringConverter;
import com.typeconverter.converter.StringToIpPortConverter;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class formattingConversionService {

    @Test
    void eeee() {
        // 이름이 중복이라 별로다.
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        //포맷터 등록
        conversionService.addFormatter(new MyNumberFormatter());
        //컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080",IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
        //포맷터 사용
        assertThat(conversionService.convert(1000, String.class)).isEqualTo("1,000");
        assertThat(conversionService.convert("1,000", Long.class)).isEqualTo(1000L);
    }

    @Test
    void formatNumber() {
        // 이름이 중복이라 별로다.
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        //컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        //포맷터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        FormatterSample formatterSample = new FormatterSample();
        formatterSample.setNumber(10000000);
        formatterSample.setLocalDateTime(LocalDateTime.now());

        //포맷터 사용
        System.out.println(conversionService.convert(formatterSample.getNumber(), String.class));
//        assertThat(conversionService.convert(formatterSample.getNumber(), String.class)).isEqualTo("1,000");
    }
}
