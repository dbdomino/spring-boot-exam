package com.minod.itemservice.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemValidation {
    private Long id;

    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000, max=1000000)
    private Integer price;
    @NotNull
    @Max(9999)
    private Integer quantity;

    public ItemValidation() {

    }

    public ItemValidation(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}