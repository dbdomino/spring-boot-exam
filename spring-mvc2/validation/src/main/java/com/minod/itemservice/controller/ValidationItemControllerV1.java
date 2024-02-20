package com.minod.itemservice.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// http://localhost:8080/validation/v1/items
@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item()); // 이렇게 한 이유?
        // 1. input에 id name 자동등록
        // 2. 입력응답 검증실패시 이전값 다시 뿌려서 value에 자동 매핑 시켜주기. get/post랑 상관없이 Model에 담긴 값으로
        // thymeleaf에서 값 매핑 시키도록 해놓은거. 이를위해 처음부터 Item 객체를 addFrom.html에서 받아 뿌리도록 만든 것.
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
        // 여기서 상품저장 검증이 실패한다면 어떻게 할 것인가?? 요구조건 범위에 값이 안맞다면? 리다이렉트 할것인가?
        // 상품등록 실패를 다시보여주고, 값을 유지하며, 값다시 입력하라고 알림보내줘야한다.
        // 검증은 서비스에서 해야될건데, 일딴 컨트롤러에서 하나보다.
        // 검증 후 기존 오류값 다 Model에 넣어서 다시 등록Form으로 보내줘야 한다. (Post 로 보내야될거같다.)

        Map<String, String> errors = new HashMap<>(); //만약 검증시 오류가 발생하면 어떤 검증에서 오류가 발생했는지 정보를 담아둔다
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price","가격은 1,000원에서 1,000,000까지 입력해요");
        }
        if (item.getQuantity() == null || item.getQuantity()>= 9999) {
            errors.put("quantity", "수량은 최대 9,999까지 입력해요");
        }
        if (item.getPrice() != null && item.getQuantity() != null) {
            if (item.getPrice()*item.getQuantity() < 10000) { // 특정 필드가 아닌 복합 룰 검증
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. now="+item.getPrice()*item.getQuantity());
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        // 오류뿌리는건 프론트에 js로도 뿌려주던지, 아니면 동적으로 <div>를 랜더링시켜 추가적으로 보여주던지 하면 된다.
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            log.error("에러발생 : "+errors);
            return "validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

