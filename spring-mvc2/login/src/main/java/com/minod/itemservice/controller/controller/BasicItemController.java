package com.minod.itemservice.controller.controller;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor  // 이걸 씀으로써 컨트롤러를 Bean으로 생성할 때 itemRepository가 final로 되어있으니 생성자주입으로 의존성주입이 된다.
public class BasicItemController {
    private final ItemRepository itemRepository; // 생성자로 의존성 자동주입

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) { // 모델이 중요
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String save(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity, Model model) {  // Post를위해 @RequestParam으로 조건을 주자.   itemName=11&price=22&quantity=33
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item); // 뷰에 쏴주기 위해서 넣음.

        return "basic/item"; // 같은 URL, HTTP 메서드로만 구분해줌.
//        return "basic/items"; // 주의, model에 items 없으면 못읽어옴 리스트
    }
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item2, Model model) {
        itemRepository.save(item2);
//model.addAttribute("item", item); //자동 추가, 생략 가능, 즉 생략해도 @ModelAttribute 어노테이션 덕분에 자동으로 생성된다.
        return "basic/item";
    }
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // ("어트리뷰트될이름") 을 넣지 않으면, ITem 객체명에 첫번째이름 소문자로바꿔서 등록된다 ("item") 된다.
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능, 꼭 생략 해야만 후련했냐
     * model.addAttribute(item) 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId(); // 새로고침 후 상품중복등록 문제해결위해 redirect로 호출해주면, 새로고침시 이전 Post의 Form요청이 아닌, Get요청 을 새로고침한다.
    } // PRG 방법 이라고한다. 다만 redirect되므로 body영역 정보가 사라져, param형식으로 정보를 주던지, 아니면 값에맞는 view 페이지로 정확하게 연결해줘야 한다.
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); // 상태값, 뷰 템플릿에서 이 값이 있으면, 저장되었습니다. 라는메시지를 출력하기 위한 상태값.
        return "redirect:/basic/items/{itemId}"; // 뒤에 변수에 한글이나 띄워쓰기가 된다면, 문제생길수도있다. 인코딩해서 보내려면 RedirectAttributes 쓰야한다.
        // redirect에 못들어간 status는 param 형식으로 ?status=true 이렇게 들어간다. 한글이나 띄워쓰기되면 인코딩되서 들어간다.
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);
        return "basic/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    public String editFormV1(@PathVariable Long itemId, @ModelAttribute Item item) { // @ModelAttribute 를 쓰면 Model model 선언 안해도되네
        itemRepository.update(itemId, item);
        return "basic/item";
    }

    @PostMapping("/{itemId}/edit")
    public String editFormV2(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
        /*
        상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
        스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원한다.
        redirect:/basic/items/{itemId}
            컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
            redirect:/basic/items/{itemId}  {itemId} 는 @PathVariable Long itemId 의 값을 그대로 사용한다.
         */
    }


    // 중요, 새로고침시 Post 인 상품등록인 경우 중복등록되는 문제가 있다.
    /**
     * 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
     * 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add  + 상품 데이터를 서버로 전송한다.
     * 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add  + 상품 데이터를 서버로 다시 전송하게 된다.
     * 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.
     * 이 문제를 어떻게 해결할 수 있을까? -> 상품 저장 후, redirect를 시켜서 get으로 새로 요청시키면 된다. 이거를 PRG 라고 한다.
     */


    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA",10000, 30));
        itemRepository.save(new Item("itemB",20000, 50));
    }

}
