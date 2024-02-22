package com.typeconverter.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName; // 업로드 파일명
    private String storeFileName;  // 저장할 파일명 저장시 중복된 이름 방지를 위해

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
