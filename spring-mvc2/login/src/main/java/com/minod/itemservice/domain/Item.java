package com.minod.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data  // Argument, Constructor, ToString, Equals, Hashcode 다만들어준다. 위험할수 있으므로 자제하는게좋다.
@Getter
@Setter // 게터 세터만 써도 충분하긴하다.
// _this.price : 언더바_ 붙어서 내꺼의 price를 의미
//@ScriptAssert(lang="javascript", script = "_this.price * _this.quantity >= 10000") // JDK 14까지 지원, JDK 15부터 쓰려면 dependency 추가해야 함.
public class Item {
    // groups 사용방법, (groups = {SaveCheck.class, UpdateCheck.class}) 이런식으로 어노테이션마다 할당해줘야 한다. 이건 좀 아닌데

//    @NotNull(groups = UpdateCheck.class) // 수정시에만 사용하도록
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;
//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Range(min = 1000, max=1000000, groups = {SaveCheck.class, UpdateCheck.class} )
    private Integer price;
//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value=9999, groups = SaveCheck.class)
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
