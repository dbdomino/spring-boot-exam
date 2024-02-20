package com.minod.itemservice.validator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

//@Data  // Argument, Constructor, ToString, Equals, Hashcode 다만들어준다. 위험할수 있으므로 자제하는게좋다.
@Getter
@Setter // 게터 세터만 써도 충분하긴하다.
public class ItemUpdateForm {

    @NotNull
    private Long id;
    @NotBlank
    private String itemName;
    @NotNull
    @Range(min=1000, max=1000000)
    private Integer price;

//    @NotNull // 변경다르게 해보자.
    private Integer quantity;

    public ItemUpdateForm() {

    }
}
