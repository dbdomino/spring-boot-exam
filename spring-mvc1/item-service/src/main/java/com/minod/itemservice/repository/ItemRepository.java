package com.minod.itemservice.repository;

import com.minod.itemservice.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRepository {

//    private static final Map<Long, Item> store = new HashMap<>(); // static 씀, 싱글톤이면 Hash맵보단 컨커런트해시맵
    private static long sequence = 0L; // static atomic long 쓰기

    // static 씀, 싱글톤이면 Hash맵보단 컨커런트해시맵
    private static final Map<Long, Item> store = new ConcurrentHashMap<>();
    private static AtomicLong sequenceAtomic = new AtomicLong(); // static atomic long 쓰기 // 이게뭐냐면, Long을 wrapping 시킨것, get(), set() 으로 불러오거나 저장한다.

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); // 객체들 모두 출력,
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }
    public void clearStore() {
        store.clear();
    }

}
