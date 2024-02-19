package com.minod.itemservice.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;
    /*
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "errors");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }
basenames : 설정 파일의 이름을 지정한다.
    - messages 로 지정하면 messages.properties 파일을 읽어서 사용한다.
    - 추가로 국제화 기능을 적용하려면 messages_en.properties , messages_ko.properties와 같이 파일명 마지막에
    언어 정보를 주면된다. 만약 찾을 수 있는 국제화 파일이 없으면messages.properties (언어정보가 없는 파일명)를 기본으로 사용한다.
    - 파일의 위치는 /resources/messages.properties 에 두면 된다.
    - 여러 파일을 한번에 지정할 수 있다. 여기서는 messages , errors 둘을 지정했다.
defaultEncoding : 인코딩 정보를 지정한다. utf-8 을 사용하면 된다.

스프링 부트
스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록한다
    * */

    @Test
    @DisplayName("key 에 해당하는 값 출력, locale null = default")
    void helloMessage() {
        // code : messages.properties에 있는 key
        // args : 매개변수, value영역에서 치환하는데 사용됨.
        // locale : 국가정보, 기본값 ko   Locale이 en_US 의 경우 messages_en_US  messages_en  messages 순서로 찾는다.
        String result = ms.getMessage("hello", null, null);
        System.out.println(result); // 안녕이 출력됨
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    @DisplayName("key, code 없으면 NoSuchMessageException 발생")
    void notFoundMessageCode() {
//        ms.getMessage("no_code", null, null);  // NoSuchMessageException 발생
        assertThatThrownBy( () -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
        // 예외 발생한게 NoSuchMessageException 이면 성공
    }

    @Test
    @DisplayName("defaultMessage 선택")
    void noCode() {
        String result = ms.getMessage("no_code", null, "--",null);  // locale코드없어 메시지 못찾으면 defualtMessage 반환

        assertThat(result).isEqualTo("--"); // defualt message와 일치하면 성동
//        assertThatThrownBy( () -> ms.getMessage("no_code", null, null))
//                .isInstanceOf(NoSuchMessageException.class);
        // 예외 발생한게 NoSuchMessageException 이면 성공
    }

    @Test
    @DisplayName("args 선택")
    void argumentMessage() {
        // hello.name=안녕 {0}   Spring 단어를 매개변수로 전달 안녕 Spring
        String result = ms.getMessage("hello.name", new Object[]{"스프링아"}, null);
        assertThat(result).isEqualTo("안녕 스프링아");
    }

    @Test
    @DisplayName("locale 선택")
    void defaultLang() {
        // locale 선택
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
        System.out.println(Locale.KOREA);  // ko_KR
        System.out.println(Locale.KOREAN); // ko
        System.out.println(Locale.UK);     // en_GB
    }

    @Test
    @DisplayName("국제화 파일 선택 en")
    void enLang() {
        // Locale 정보가 없는 경우 Locale.getDefault() 을 호출해서 시스템의 기본 로케일을 사용합니다.
        //예) locale = null 인 경우 시스템 기본 locale 이 ko_KR 이므로 messages_ko.properties 조회 시도합니다.
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
