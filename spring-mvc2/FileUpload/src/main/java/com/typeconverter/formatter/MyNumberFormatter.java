package com.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
    // locale기준으로 숫자->문자로 표현, 문자->숫자로 표현한다.
    // 내부에 지정된 포멧 형식으로 반환되는데, 금액정보를 출력하기 위해 사용한다.
    // 즉 화폐단위문자형으로 바꿔주기위해 사용  1000 -> "1,000"  , "1,000" -> 1000

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        // "1,000" -> 1000 으로 바꿔줌.
        // 매번 locale 정보를 넣을수는 없다. 자바에서 제공되는 NumberFormat 을 쓰면 된다.
        NumberFormat format = NumberFormat.getNumberInstance();
        NumberFormat format2 = NumberFormat.getNumberInstance(locale);

        return format.parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);

        //NumberFormat instance = NumberFormat.getInstance();
        //String format = instance.format(object);

        return NumberFormat.getInstance().format(object); // ctrl + alt + n
    }
}
