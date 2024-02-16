package com.minod.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

//@Data  // Argument, Constructor, ToString, Equals, Hashcode 다만들어준다. 위험할수 있으므로 자제하는게좋다.
@Getter
@Setter // 게터 세터만 써도 충분하긴하다.
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public  Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
