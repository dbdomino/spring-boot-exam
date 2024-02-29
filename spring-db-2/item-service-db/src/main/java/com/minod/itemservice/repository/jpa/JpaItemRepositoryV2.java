package com.minod.itemservice.repository.jpa;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA 기본제공 인터페이스로 구현된 Repository를 리펙토링해서
 * ItemRepository 에 맞는 메소드로 변경,
 * JpaItemRepository 의 JPA 메소드와 다른 기본제공 메서드를 통해 쿼리 구성함.
 */

@Slf4j
@Repository
@Transactional  // 필수, JPA의 모든 변경은 트랜잭션 안에서 이루어지므로 넣어준다고 함.
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {
    private final SpringDataJpaItemRepository repository;

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id); // 스프링 데이터 JPA가 제공하는 메소드
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        // 동적쿼리 생성위해 스프링 데이터 JPA에서 구현방법.
        if (StringUtils.hasText(itemName) && maxPrice != null) {
//return repository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice); // 쿼리메서드
            return repository.findItems("%" + itemName + "%", maxPrice); // 같은거
        } else if (StringUtils.hasText(itemName)) {
            return repository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) {
            return repository.findByPriceLessThanEqual(maxPrice);
        } else {
            return repository.findAll();
        }
    }
}
