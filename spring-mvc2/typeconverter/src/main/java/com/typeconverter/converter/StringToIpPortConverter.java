package com.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
    @Override
    public IpPort convert(String source) {
        log.info("source");
        String[] split = source.split(":");
        return new IpPort(split[0],Integer.parseInt(split[1]));

    }
}
