package com.minod.itemservice.domain;

public enum ItemType {
    BOOK("서적"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    // getter가 없으면 description은 못 꺼낸다.

    public String getDescription() {
        return description;
    }
}
