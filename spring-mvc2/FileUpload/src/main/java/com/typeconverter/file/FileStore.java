package com.typeconverter.file;

import com.typeconverter.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
// 파일 저장과 관련된 업무 처리
public class FileStore {
    @Value("${file.directory}")
    private String fileDir;

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                /*UploadFile uploadFile = storeFile(multipartFile); // 라인합치기 ctrl+alt+n
                storeFileResult.add(uploadFile);*/
                // 라인합치기 ctrl+alt+n
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;

    }

    // 전체경로+파일명 가져오기
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // multipartFile 로 받은걸 UploadFile 로 바꾸기위함.
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename(); // image.png 확장자포함.
        String storeFileName = createStoreFileName(originalFileName); // 저장시킬 이름

        multipartFile.transferTo(new File(getFullPath(storeFileName))); // 파일진짜로저장

        return new UploadFile(originalFileName, storeFileName); // 업로드 파일 정보 보관 객체
    }

    private String createStoreFileName(String originalFilename) {
        String Filetype = extracted(originalFilename); // 확장자만
        String uuid = UUID.randomUUID().toString();
        return uuid +"."+Filetype;
    }

    private String extracted(String originalFilename) {
        int pos = originalFilename.lastIndexOf("."); // 점의 마지막 위치 가져오기
        if (pos <0) return "none"; // 확장자없는파일일경우
        return originalFilename.substring(pos+1); // 점의 마지막부터 긁어옴, 확장자
    }
/*    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }*/


}
