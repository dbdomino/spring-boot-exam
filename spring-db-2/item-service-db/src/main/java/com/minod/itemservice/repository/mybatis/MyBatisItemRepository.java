package com.minod.itemservice.repository.mybatis;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {
    private final ItemMapper itemMapper; // 의존관계 주입

    @Override
    public Item save(Item item) {
        log.info("itemMapper class = {}",itemMapper.getClass()); // itemMapper class = class jdk.proxy3.$Proxy95 동적 프록시 객체로 만들어진 itemMapper 구현체 인스턴스
        itemMapper.save(item);
        return item; // 이건 생성결과 전달 위해 넘김.

    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id); // Optional로 뱉는 이유? Optional로 뱉도록 하고싶으니까. null뱉더라도 오류안내려
//        return null;
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return itemMapper.findAll(cond); // 검색조건 cond 에 맞는 검색결과 List로 출력, 여러개 일수있으니 음.
//        return null;
    }
}
