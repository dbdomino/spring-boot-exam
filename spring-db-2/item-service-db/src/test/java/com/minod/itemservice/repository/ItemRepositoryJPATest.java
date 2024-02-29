package com.minod.itemservice.repository;

import com.minod.itemservice.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Mybatis 적용됬는지 테스트
 */
@Slf4j
@Transactional
@SpringBootTest
class ItemRepositoryJPATest {

    @Autowired ItemRepository itemRepository;

    @Test
//    @Commit    // 성공시 commit되기를 원하면 이거붙여주면 된다.
    @Transactional
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);
        log.info("item before : {}",item);

        //then
        Item findItem = itemRepository.findById(item.getId()).get();
        log.info("item after : {}",findItem);
        // 같은 객체를 바라보진 않지만, 같은 값을 바라보는지 테스트는 해야된다.
        assertThat(findItem.getId()).isEqualTo(savedItem.getId());
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when
        ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
        itemRepository.update(itemId, updateParam);

        //then
        Item findItem = itemRepository.findById(itemId).get();
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

    @Test
    void findItems() {
        //given
        Item item1 = new Item("itemA-1", 10000, 10);
        Item item2 = new Item("itemA-2", 20000, 20);
        Item item3 = new Item("itemB-1", 30000, 30);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        ItemSearchCond cond = new ItemSearchCond();
        cond.setMaxPrice(50000);

        itemRepository.findAll(cond).iterator().forEachRemaining(s -> System.out.println(s));
    }

    void test(String itemName, Integer maxPrice, Item... items) {
        List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));
        assertThat(result).containsExactly(items);
        // 메모리 리퍼지터리에서 비교기준은 객체주소로 해도 됬었다. 그러나 이제 db에서 끌어오는건 객체주소로 같을수가 없다. 테스트코드 새로필요
    }
}
