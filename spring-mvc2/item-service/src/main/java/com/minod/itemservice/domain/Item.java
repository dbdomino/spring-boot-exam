package com.minod.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data  // Argument, Constructor, ToString, Equals, Hashcode 다만들어준다. 위험할수 있으므로 자제하는게좋다.
@Getter
@Setter // 게터 세터만 써도 충분하긴하다.
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    // 추가 변수, 옵션임.
    private Boolean open; // 판매여부
    private List<String> regions; // 등록지역
    private ItemType itemType; // 상품종류
    private String deliveryCode; // 배송코드, 배송방식

    public Item(String itemName) {
        this.itemName = itemName;
    }

    public  Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
