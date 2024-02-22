package com.typeconverter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {
    @Value("${file.directory}")
    private String fileDirectory;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={} ", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {} ",itemName);

        // 서블릿이 제공하는 Part 사용해서 파일정보 확인하기, request를 통해서 확인해야 하는 문제가 있다. request의 모든 Part를 까서 확인하는 수 밖에...
        Collection<Part> parts = request.getParts();  // 핵심,  Content-Type : multipart/form-data 로 올때 확인가능함.
        log.info("parts = {}",parts);

        String os = System.getProperty("os.name").toLowerCase();
        log.info("os : {}",os);

        for (Part part : parts) {  // iter 치면 가장 가까이있는 컬렉션을 루프로 돌릴 수 있다.
            log.info("===---PART---===");
            log.info("name={}", part.getName()); // 파트 이름

            Collection<String> headerNames = part.getHeaderNames(); // 사실 JPA보단 DB가더중요하다.
            for (String headerName : headerNames) { // 파트의 헤더이름과 헤더값
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }
            //위의 반복에서 특정 헤더의 이름을 뽑아내려면 어떻게 해야할까? 파싱해서 찾아야하나? 찾기위한 메서드를 Part(Jakarta)에서 제공한다. (파싱해서 꺼내는 소스가 만들어져있다.)

            // 편의메소드 filename
            log.info("submittedFileName={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            //데이터 읽기
            // binary 데이터를 문자 데이터로 바꾸려면 Character를 항상 설정해줘야 한다. UTF-8
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//            log.info("body={}", body); // 이미지면 깨져서 나오고, 용량크면 로그 너무많다. 주석쳐야한다.

            //파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDirectory + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);
            }

        }

        return "upload-form";
    }
}
