package com.minod.itemservice.controller.controller;

import com.minod.itemservice.domain.DeliveryCode;
import com.minod.itemservice.domain.Item;
import com.minod.itemservice.domain.ItemType;
import com.minod.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("regions") // 어노테이션 중에 @ModelAttribute 라는게 있다.
    // 여기 컨트롤러 안에서 어떤 모델에라도 위에 선언된 "regions"라는 이름으로 return 결과를 addAttribute 시켜줍니다.
    // 파라미터에 @ModelAttribute 를 넣어서 객체로 request값을 매핑시키는 방법과는 다른 방법입니다.
    // 해당 컨트롤러 내부에 모든 Model에 정보를 담아야 할 경우 유용하게 사용할 수 있는 기능입니다. 다만 계속호출됩니다.
    // 성능에는 안좋을 수 있으니 동적으로 변하는게 아니라면, 고정적으로 생성해놓고 주입시켜 쓰게 하는것도 좋습니다.
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {  // enum의 value를   values()다보니 배열로 반환합니다.
        return ItemType.values();
    }


    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();  // 자바 객체를 ArrayList에 저장시켜 반환합니다.
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    // 타임리프가 지원하는 form기능을 쓰려면 조건, Model로 item을 넘겨줘야 한다.
     @GetMapping("/add")
    public String addForm(Model model)
     {
         model.addAttribute("item", new Item("item001"));
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}",item.getOpen());
        log.info("item.regions={}",item.getRegions());
        log.info("item.itemType={}",item.getItemType());
        log.info("item.deliveryCode={}",item.getDeliveryCode());


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

