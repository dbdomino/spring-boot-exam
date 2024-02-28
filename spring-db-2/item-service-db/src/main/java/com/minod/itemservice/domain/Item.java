package com.minod.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

//@Data  // Argument, Constructor, ToString, Equals, Hashcode 다만들어준다. 위험할수 있으므로 자제하는게좋다.
@Data
@Entity // 이 에노테이션이 있어야 JPA가 인식할 수 있다. @Entity 가 붙은 객체를 JPA에서는 엔티티라 한다.
public class Item {

    @Id  // 기본키일 경우 알려줘야 한다고 함. 테이블의 PK와 해당 필드를 매핑한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 ID를 생성해주는 전략이므로 GenerationType.IDENTITY를 써줘야 된다고 함.
    private Long id;
    @Column(name = "item_name", length = 10) // length 는 안써도 됨.
    private String itemName;

    private Integer price;
    private Integer quantity;

    // jpa에선 엔티티와 일치하지 않는 필드 있으면 오류 발생 [오류: "delivery_code" 칼럼은 "item" 릴레이션(relation)에 없음,
    // [insert into public.item (delivery_code,item_name,item_type,open,price,quantity) values (?,?,?,?,?,?)]; SQL [insert into public.item (delivery_code,item_name,item_type,open,price,quantity) values (?,?,?,?,?,?)]
//    private Boolean open; // 판매여부
//    private ItemType itemType; // 상품종류
//    private String deliveryCode; // 배송코드, 배송방식

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
