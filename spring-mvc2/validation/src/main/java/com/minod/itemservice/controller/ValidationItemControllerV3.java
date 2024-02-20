package com.minod.itemservice.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.domain.SaveCheck;
import com.minod.itemservice.domain.UpdateCheck;
import com.minod.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// http://localhost:8080/validation/v3/items
@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
/* 직접 만든 Validator(검증기) 안씀, 스프링부트는 자동으로 검증기를 등록해주며, 글로벌 Valitator를 직접 등록하면, 스프링 부트는 Bean Validator를 글로벌'Validator'로 등록하지 않는다.
그래서 아래부분은 제거함.
    private final ItemValidator itemValidator;

    @InitBinder // Validator 분리, InitBinder 라는 어노테이션과 WebDataBinder 라는게 있다.
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }
*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item()); // 이렇게 한 이유?
        // 1. input에 id name 자동등록
        // 2. 입력응답 검증실패시 이전값 다시 뿌려서 value에 자동 매핑 시켜주기. get/post랑 상관없이 Model에 담긴 값으로
        // thymeleaf에서 값 매핑 시키도록 해놓은거. 이를위해 처음부터 Item 객체를 addFrom.html에서 받아 뿌리도록 만든 것.
        return "validation/v3/addForm";
    }

    // 스프링 부트는 자동으로 LocalValidatorFactoryBean(밸리데이터 팩토리 빈) 을 글로벌 빈 Validator(검증기)로 등록한다.
    // 이덕분에 @Validated를 객체에 붙이면, 검증기에 @Validated붙은 객체를 넣고 오류정보를 BindingResult에 넣어준다.<커스텀하게 등록한 검증과는 다른 오류결과가 나오는 것을 알 수 있다.>
    @PostMapping("/add") // @Valid 는 자바 표준, @Validated는 스프링 표준, 결과는 같지만, Groups기능은 스프링표준(@Validated) 에서만 사용가능.
    public String addItem(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // Bean Validation Group  @Validated(SaveCheck.class) 컨트롤러마다 특정그룹의 어노테이션 정보로 검증하겠다는 소리
        //특정 필드 validation이 불가한 요청, 직접 reject로 구현
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
        // Bean Validation Group  @Validated(SaveCheck.class) 컨트롤러마다 특정그룹의 어노테이션 정보로 검증하겠다는 소리
        //특정 필드 validation이 불가한 요청, 직접 reject로 구현
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

