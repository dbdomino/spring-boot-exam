package com.minod.itemservice.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// http://localhost:8080/validation/v2/items
@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder // Validator 분리, InitBinder 라는 어노테이션과 WebDataBinder 라는게 있다.
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item()); // 이렇게 한 이유?
        // 1. input에 id name 자동등록
        // 2. 입력응답 검증실패시 이전값 다시 뿌려서 value에 자동 매핑 시켜주기. get/post랑 상관없이 Model에 담긴 값으로
        // thymeleaf에서 값 매핑 시키도록 해놓은거. 이를위해 처음부터 Item 객체를 addFrom.html에서 받아 뿌리도록 만든 것.
        return "validation/v2/addForm";
    }

    // BindingResult 처음도입, 조건에 안맞아 fieldError ObjectError 날 경우 새로운요청에 값이 남게하기위해 V2진행
//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // BindingResult 스프링이 제공하는 오류제공 객체, errors Map을 대체하여 view에 전달시킬 수 있습니다.
        // 주의사항 @ModelAttribute 바로 다음으로 BindingResult 를 선언해줘야 한다고 합니다. 순서 매우중요. 안그러면 동작은 하더라도, 타입이 안맞는 에러는 못걸러냄.

        // BindingResult 로 출력하려면, 별도로 view에서 조정 해줘야 한다고합니다. 귀찮
        //검증 로직  에러가 있다면, FieldError, ObjectError(부모객체) 객체를 만들어 bindingResult에 담아둡니다.
        if (!StringUtils.hasText(item.getItemName())) {
//            errors.put("itemName", "상품 이름은 필수입니다.");
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수"));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            errors.put("price","가격은 1,000원에서 1,000,000까지 입력해요");
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000원에서 1,000,000까지"));
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
//            errors.put("quantity", "수량은 최대 9,999까지 입력해요");
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999까지"));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
//                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. now="+item.getPrice()*item.getQuantity());
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + item.getPrice()*item.getQuantity()));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        // 오류뿌리는건 프론트에 js로도 뿌려주던지, 아니면 동적으로 <div>를 랜더링시켜 추가적으로 보여주던지 하면 된다.
//        if (!errors.isEmpty()) {
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
//            model.addAttribute("errors", errors); //
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!StringUtils.hasText(item.getItemName())) {
            // item.getItemName(), false, null, null 추가됨(item.getItemName이거하나 추가시키기 위해 4개항목 추가) 에러시 Model에 현상태 담아서 FieldError전달
            // view에서 input태그에 th:field 로 자동완성시켜주기 위해 이짓 하는거
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null,"상품 이름은 필수"));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // 값이 Integer가 아니면 커스텀 에러메시지를 내주는게 맞아보이는데, FieldError 써서 자체에러메시지를 내는게 좋긴 하나, 옳지는 않다. 개선이 필요
            bindingResult.addError(new FieldError("item", "price" , item.getPrice(), false, null, null, "가격은 1,000원에서 1,000,000까지"));
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999까지"));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + item.getPrice()*item.getQuantity()));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add") // errors.properties로 에러코드 에러메시지 관리
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 먼저 스프링 부트가 해당 메시지 파일을 인식할 수 있게 application.properties에 다음 설정을 추가한다. (중요)
        // spring.messages.basename=messages,errors
        // 이렇게 해야 messages.properties , errors.properties 두 파일을 모두 인식한다. (생략하면 //messages.properties 를 기본으로 인식한다.)

        log.info("objectName = {}", bindingResult.getObjectName());  // 객체이름 item, @ModelAttribute 바로 다음에 bindingResult 선언해줘야 동작함. 불편
        log.info("targetName = {}", bindingResult.getTarget());   // 객체  com.minod.itemservice.domain.Item@1add3535

        if (!StringUtils.hasText(item.getItemName())) {
            // FieldError 영역에 codes 부분에 String배열 추가하는 이유 -> 첫번째 에러코드 없으면 두번째 에러코드... 이런식으로 넣으려고 배열 넣는다고 함.
            // 이렇게하면 defaultMessage를 null로 하더라도 errors.properties에서 code에 해당하는 value를 defaultMessage를 가져오게된다. value에 arguement필요한건 객체에 선언한 new Object[]에서 빼간다. 상당히 불편한 소스이다. 모르면 못쓰는 코드.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null,null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // argument는 errors.properties에 argument 넣도록 설정해놔서 넣어줄수밖에없음.
            bindingResult.addError(new FieldError("item", "price" , item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[] {9999}, null));
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
                int resultPrice = item.getPrice() * item.getQuantity();
                bindingResult.addError(new ObjectError("item", new String[] {"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add") // errors.properties로 에러코드 에러메시지 관리2, addError대신 rejectValue 메소드 사용
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 먼저 스프링 부트가 해당 메시지 파일을 인식할 수 있게 application.properties에 다음 설정을 추가한다. (중요)
        // spring.messages.basename=messages,errors
        // 이렇게 해야 messages.properties , errors.properties 두 파일을 모두 인식한다. (생략하면 //messages.properties 를 기본으로 인식한다.)

        //검증에 실패하면 입력 폼으로, 뒤에 한번더 넣은 이유는, 커스텀 에러 잡기위해서, 지금은 자동으로 스프링에서 제공하는 에러대응을 위해
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        log.info("objectName = {}", bindingResult.getObjectName());  // 객체이름 item, @ModelAttribute 바로 다음에 bindingResult 선언해줘야 동작함. 불편
        log.info("targetName = {}", bindingResult.getTarget());   // 객체  com.minod.itemservice.domain.Item@1add3535

        if (!StringUtils.hasText(item.getItemName())) { // FieldError, ObjectError 길다 번거롭다, 다른거쓰자
            // rejectValue
            bindingResult.rejectValue("itemName", "required"); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            // defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지, 자신잇으면 null 써도됨.
            // 타입이 안맞는 오류일 때, defaultMessage말고 자바오류 메시지를 뱉는다.
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000},null); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999, 1000000},null); // 에러코드의 .(쉼표) 기준 첫글자만 errorCode영역에 넣는다.
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
                int resultPrice = item.getPrice() * item.getQuantity();
                // rejectValue 가 아닌 reject 씀 -> new ObjectError 를 대신함.
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add") // 별도로 validate 검증 로직을 ItemValidator로 분리
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // 스프링 기본검증 위해 먼저 선언
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        itemValidator.validate(item,bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add") // Contriller에 @InitBinder 로 WebDataBinder에다 itemValidator 추가하기, 해당 컨트롤러에 검증기를 자동으로 적용, 검증필요한 객체에 @Validated 를 붙여서 자동검증이 지원된다.
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // @Validated 가 붙은건 WebDataBinder가 검증을 실행하는데, 검증하는 기준의 구분은 어떻게 하는가?
        // WebDataBinder에 들어간 ItemValidator의 supports 메소드로 검증할 인스턴스 형식이 맞는지 확인 후 ture이면 검증들어간다고한다.
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

