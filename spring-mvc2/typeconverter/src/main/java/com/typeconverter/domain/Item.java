package com.typeconverter.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
// 상품 저장된 후 불러오는 폼이다.
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
