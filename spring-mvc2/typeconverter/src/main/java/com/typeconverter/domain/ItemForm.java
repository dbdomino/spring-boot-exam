package com.typeconverter.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {
// 상품 저장용 폼이다.
    private Long id;
    private String itemName;
    private MultipartFile attachFile;  // MultipartFile은 @ModelAttribute 에서 사용할 수 있다.
    private List<MultipartFile> imageFiles; // 이미지를 다중 업로드 하기 위해 MultipartFile 를 사용했다.
}
