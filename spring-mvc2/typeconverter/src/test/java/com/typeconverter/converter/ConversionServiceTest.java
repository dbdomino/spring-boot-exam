package com.typeconverter.converter;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionServiceTest {
    @Test
    void conversionService(){
        // 인터페이스 분리 원칙 - ISP(Interface Segregation Principle)
        //인터페이스 분리 원칙은 클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.
        /**
         * 컨버터를 등록할 때는 StringToIntegerConverter 같은 타입 컨버터를 명확하게 알아야 한다.
         * 반면에 컨버터를 사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다.
         */

        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 사용
        assertThat(conversionService.convert("10",Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");
        IpPort ipPort = conversionService.convert("127.0.0.1:8080",IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
        String ipPortString = conversionService.convert(new IpPort("127.0.0.1",8080), String.class);
        assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
    }
}
