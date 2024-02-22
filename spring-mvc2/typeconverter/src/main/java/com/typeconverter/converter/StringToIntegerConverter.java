package com.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

// 컨버터 중요한 부분, org.springframework.core.convert.converter.Converter;
@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        log.info("StringtoInteger source = {}",source);
        return Integer.valueOf(source);
    }
}
