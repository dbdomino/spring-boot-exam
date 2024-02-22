package com.typeconverter.formatter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Data
public class FormatterSample {
    @NumberFormat(pattern = "###,###")
    private Integer number;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 국제적 표준 형식
    private LocalDateTime localDateTime;
}