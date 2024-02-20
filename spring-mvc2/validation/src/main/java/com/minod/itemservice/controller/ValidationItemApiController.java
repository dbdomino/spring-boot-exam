package com.minod.itemservice.controller;

import com.minod.itemservice.validator.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    /*
    @ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.
    @RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 주로 API JSON 요청을 다룰때 사용한다.

    (@Validated @ModelAttribute 객체, BindingResult bindingResult)   이런 차이가 있을것이다.
    (@Validated @RequestBody 객체, BindingResult bindingResult)

    큰 차이는, HTTP 요청파라미터를 처리하는 @ModelAttribute 는 각 필드 단위로 하나하나씩 적용, 뭔가 필드하나 타입다르게 오더라도 벨리데이션 체크가능
    @RequestBody 는 전체 객체 단위로 적용하여, 반드시 form객체가 성공적으로 만들어져야만 내부 컨트롤러 동작 가능함. 그래야 @Valid, @Validation 도 적용가능함.
     */

    /*
    성공 {"itemName":"hello", "price":1000, "quantity": 10}
    실패 {"itemName":"hello", "price":A, "quantity": 10}   HttpMessageNotReadableException: JSON parse error: Invalid UTF-8 start byte 0x8b]
    검증오류 {"itemName":"hello", "price":500, "quantity": 10}   form객체는 만들어졋으나 오류정보가 bindingResult에 들어감, Field error in object 'itemSaveForm' on field 'price': rejected value [500]; codes [Range.itemSaveForm.price,Range.price,Range.java.lang.Integer,Range];

     */
    @ResponseBody
    @PostMapping("/add")
    public Object addItem(@Validated @RequestBody ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출"); // 실패(검증오류와 다름)시 이게 호출안된다. input이 잘못 들어오면, form에서 객체로 못바뀌고 에러 발생시킴, log.info()이줄까지 못들어온다.

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors(); //  -> 오류발생시 이걸로 출력하도록 설정, 다른걸로 오류정보 뱉게끔 커스터마이징도 가능함.
        }
        log.info("성공 로직 실행");
        return form;
    }
}
