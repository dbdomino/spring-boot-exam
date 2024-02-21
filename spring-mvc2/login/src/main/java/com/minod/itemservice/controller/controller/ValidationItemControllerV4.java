package com.minod.itemservice.controller.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.domain.validator.ItemSaveForm;
import com.minod.itemservice.domain.validator.ItemUpdateForm;
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

// http://localhost:8080/validation/v4/items
@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item()); // 이렇게 한 이유?
        // 1. input에 id name 자동등록
        // 2. 입력응답 검증실패시 이전값 다시 뿌려서 value에 자동 매핑 시켜주기. get/post랑 상관없이 Model에 담긴 값으로
        // thymeleaf에서 값 매핑 시키도록 해놓은거. 이를위해 처음부터 Item 객체를 addFrom.html에서 받아 뿌리도록 만든 것.
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//      @ModelAttribute로   model.addAttribute("itemSaveForm",form) 이렇게 담긴다. 이름변경 가능

        //특정 필드 validation이 불가한 요청, 직접 reject로 구현
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v4/addForm";
        }

        // ItemSaveForm 로 받은걸 Item으로 바꿔서 Repository에 넘겨야 된다. 서비스에 넘겨서 처리하는게 더 낫지않나?
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
        //특정 필드 validation이 불가한 요청, 직접 reject로 구현
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.error("에러발생 bindingResult.hasErrors={}", bindingResult); // bindingResult는 Model에 담을필요 없나봄
            return "validation/v4/editForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

