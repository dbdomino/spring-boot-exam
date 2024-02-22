package com.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring/v1")
public class SpringUploadController {
    @Value("${file.directory}")
    private String fileDirectory;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    /** form 으로 Post 요청시 아래 2개요소 request로 받고 파라미터 객체로 매핑시켜 줌. 이전 서블릿에서 request로 요청한거에 Parts 콜렉션을가지고 Part 나눠서 읽고 이럴 필요가 없다.
     * @RequestParam String itemName,   아이템 이름
     * @RequestParam MultipartFile file  파일 내용
     */
    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file,
                           HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if (!file.isEmpty()) { // 핵심
            String fullPath = fileDirectory + file.getOriginalFilename(); // 파일명
            log.info("파일 저장 fullPath={}", fullPath); // 파일경로
            file.transferTo(new File(fullPath)); // 핵심 파일저장
        }
        return "upload-form";
    }
}
