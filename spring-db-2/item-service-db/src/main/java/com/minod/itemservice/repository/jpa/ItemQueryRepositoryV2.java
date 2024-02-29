package com.minod.itemservice.repository.jpa;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.minod.itemservice.domain.QItem.item;

/** 동적쿼리만 담당하는 Querydsl 전용 리포지터리
 *  ItemQueryRepositoryV2, ItemServiceV2와 연계
 */

@Slf4j
@Repository
public class ItemQueryRepositoryV2 {
    private final JPAQueryFactory query;
    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond){
        return query.select(item)
                .from(item)
                .where(likeItemName(cond.getItemName()), maxPrice(cond.getMaxPrice()))
                .fetch();
    }

    // 쿼리조각의 재사용
    private BooleanExpression likeItemName(String itemName) { // ibatis 말고 queryDSL BooleanExpression
        // 사용조건, QItem을 static으로 등록시켜야 함.
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }
    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }

}
