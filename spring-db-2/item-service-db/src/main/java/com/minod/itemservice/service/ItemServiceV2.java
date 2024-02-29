package com.minod.itemservice.service;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import com.minod.itemservice.repository.jpa.ItemQueryRepositoryV2;
import com.minod.itemservice.repository.jpa.ItemRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional // 트랜잭션 지원을 위해
public class ItemServiceV2 implements ItemService{

    private final ItemQueryRepositoryV2 itemQueryRepositoryV2; // 쿼리 dsl 리포지터리
    private final ItemRepositoryV2 itemRepositoryV2; // 스프링 데이터 JPA 리포지터리


    @Override
    public Item save(Item item) {
        return itemRepositoryV2.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = itemRepositoryV2.findById(itemId).orElseThrow(); // findById 옵셔녈 뱉게 해놔서, orElse 추가
        findItem.setItemName(updateParam.getItemName());  // 서비스에서 수정을 구현하는 이유는? 서비스에서 트랜잭션 적용시킬려고
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepositoryV2.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond itemSearch) {
        return itemQueryRepositoryV2.findAll(itemSearch);
    }
}
