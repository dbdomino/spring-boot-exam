package com.minod.itemservice.repository.jpa;

import com.minod.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
    // 기본제공 CRUD는 추가됨.
}
