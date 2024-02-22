package com.typeconverter.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();

        Integer result = converter.convert("20");

        assertThat(result).isEqualTo(20);
    }

    @Test
    void integerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();

        String result = converter.convert(10);

        assertThat(result).isEqualTo("10");
    }

    @Test
    void stringToIpPort() {
        StringToIpPortConverter converter= new StringToIpPortConverter();
        String source = "127.0.0.1:8080";

        IpPort ipPort = converter.convert(source);

        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));
        // isEqualTo가 객체의 값만 비교해주는건가??
    }

    @Test
    void ipPortToString(){
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);

        String result = converter.convert(source);

        assertThat(result).isEqualTo("127.0.0.1:8080");

    }

    @Test
    void assertThatTest() {
        StringToIpPortConverter converter= new StringToIpPortConverter();
        String source = "127.0.0.1:8080";

        IpPort ipPort1 = new IpPort("127.0.0.1", 8080);
        IpPort ipPort2 = new IpPort("127.0.0.1", 8081);
        IpPort ipPort3 = new IpPort("127.0.0.1", 8080);
        System.out.println(ipPort1);
        System.out.println(ipPort2);
        System.out.println(ipPort3);

        assertThat(ipPort1).isEqualTo(ipPort3);
        // isEqualTo가 객체의 값만 비교해주는건가?? 아니다. 객체 주소를 비교한다.
        // 생성자에 변수가 같다면 같은 객체로 인식하여 new를 한다 해도 같은 객체를 바라보네.
    }
}
