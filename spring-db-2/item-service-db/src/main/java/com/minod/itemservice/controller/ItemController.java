package com.minod.itemservice.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import com.minod.itemservice.service.ItemService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/items/dbform")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostConstruct // 미리선언 , 메모리 리포지터리 쓸때 하던거, DB 리포지터리변환시 불필요
    // @EventListener(ApplicationReadyEvent.class)  이걸로 스프링 컨테이너 동작완료 후 넣어도 되는 부분이라,
    // @PostConstruct 보다 나음.
    public void init() {
//        itemService.save(new Item("itemA",10000, 30));
//        itemService.save(new Item("itemB",20000, 50));
    }

    @GetMapping
    public String items(@ModelAttribute("itemSearch") ItemSearchCond itemSearch, Model model) {
        List<Item> items = itemService.findItems(itemSearch);
        model.addAttribute("items", items);
        return "dbform/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemService.findById(itemId).get();
        model.addAttribute("item", item);
        return "dbform/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "dbform/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/dbform/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemService.findById(itemId).get();
        model.addAttribute("item", item);
        return "dbform/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute ItemUpdateDto updateParam) {
        itemService.update(itemId, updateParam);
        return "redirect:/items/dbform/{itemId}";
    }

}
