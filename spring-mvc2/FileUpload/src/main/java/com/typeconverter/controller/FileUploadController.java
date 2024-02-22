package com.typeconverter.controller;


import com.typeconverter.domain.Item;
import com.typeconverter.domain.ItemForm;
import com.typeconverter.domain.ItemRepository;
import com.typeconverter.domain.UploadFile;
import com.typeconverter.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileUploadController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newFile(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveFile(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        /*MultipartFile attachFile = form.getAttachFile();
        UploadFile uploadFile = fileStore.storeFile(attachFile);*/
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile()); // 파일 실제저장

        /*List<MultipartFile> imageFiles = form.getImageFiles();
        List<UploadFile> uploadFiles = fileStore.storeFiles(imageFiles);*/
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles()); // 파일 실제저장

        // db에 정보 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }
    // 이미지를 보기위한 /images/파일명  으로 전달하기 위한 컨트롤러도 만들어준다.
    // 정적 컨텐츠 전달하기 위한 컨트롤러
    @ResponseBody
    @GetMapping(value = "/images/{filename}", produces = "image/*") // 이미지 단일출력시 아래처럼 바이너리로 보내면 문자로만나와서 produces 로 컨텐츠타입 고정
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException, FileNotFoundException {
//        return new UrlResource("file:"+fileStore.getFullPath(filename)); // 파일경로에 접근해서 파일을 스트림으로 반환시킴.
        // UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환한다.
        // 그러다보니 브라우저에 경로에서 직접 열면 깨져서 나온다. produces의 컨텐츠타입 지정으로 해결
        // 다만 올바른 파일경로 아닐경우 FileNotFoundException 뜨면서 500에러를 뱉는다, 에러처리 필요.

        String res = fileStore.getFullPath(filename);
        // MediaType.parseMediaType(Files.probeContentType(res)); 파일의 미디어타입 확인
        InputStream in = new FileInputStream(res);
        Resource resource = new InputStreamResource(in);

        return resource;

    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException{
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:"+fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        // 한글 또는 띄워쓰기시 파일다운로드 안될 수 있어서 파일명으로 인코딩해주기
        String encodedUploadFileName= UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        log.info("encodedUploadFileName={}", encodedUploadFileName);

        // 규약, 첨부파일 클릭시 파일이 그냥 열려벼리는데, 이미지라면 바이너리로 깨져서 나옴.
        // 헤더에 정보를 붙여줘야 해결된다고 함. CONTENT_DISPOSITION 헤더에 아래정보 추가해야 다운받기 됨. 규칙임.
//        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\"";
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\""; // 인코딩된 파일명을 넣어줌.



//        return ResponseEntity.ok().body(resource);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
