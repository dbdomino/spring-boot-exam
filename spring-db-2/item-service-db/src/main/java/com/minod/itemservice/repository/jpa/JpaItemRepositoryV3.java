package com.minod.itemservice.repository.jpa;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.domain.QItem;
import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.ItemSearchCond;
import com.minod.itemservice.repository.ItemUpdateDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.minod.itemservice.domain.QItem.item;

/** JPA와 QueryDSL 같이 쓰기
 *  JPA(EntityManager) , QueryDSL(JPAQueryFactory)
 */

@Slf4j
@Repository
@Transactional  // 필수, JPA의 모든 변경은 트랜잭션 안에서 이루어지므로 넣어준다고 함.
//@RequiredArgsConstructor // 생성자 별도 주입 필요.
public class JpaItemRepositoryV3 implements ItemRepository {
//    private final SpringDataJpaItemRepository repository;
    private final EntityManager em; // 자동주입
    private final JPAQueryFactory query; // QueryDSL에서 제공, JPA에서 JPQL 빌드하려면 필요.

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em); // 여기서 바로 생성해버림.
    }

    @Override
    public Item save(Item item) {
        em.persist(item); // jpa 저장
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId); // 하나 찾는건 이렇게, 간단히
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item); // 스프링 데이터 JPA가 제공하는 메소드
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        // 동적쿼리 생성위해 스프링 데이터 JPA 이용말고 QueryDSL 이용 (
        // Q파일   Q+Class이름  으로된 Entity 클래스 생성됨. Q파일의 역할?
        QItem qitem = new QItem("i");// 파라미터는 alias로 사용할 철자를 넣음.

        // 동적 조건을 아래 where 넣는 방법 1
        BooleanBuilder builder = new BooleanBuilder(); // QueryDSL이 제공하는 타입.
        if (StringUtils.hasText(itemName)) {
            builder.and(qitem.itemName.like("%"+itemName+"%"));
        }
        if (maxPrice != null) {
            builder.and(qitem.price.loe(maxPrice));
        }
        // 동적 조건을 아래 where 넣는 방법 2
        // 위 조건들을 메서드로 만들어 사용
        // private BooleanExpression likeItemName(String itemName) { ... }
        // private BooleanExpression maxPrice(Integer maxPrice) { ... }

        List<Item> itemList = query.select(qitem)
                .from(qitem)
                .where()
                .fetch();
        // QItem 안에 내부적으로 가진 static item 객체가 있는데, 이걸 써도 된다.
        List<Item> itemList2 = query.select(item)
                .from(item)
                .where()
                .fetch();
        // 외부 조건에 따라 동적으로 조건 추가
        List<Item> itemList3 = query.select(item)
                .from(item)
                .where(builder)
                .fetch();
        // 동적쿼리 아닐 경우 직접 조건넣기도 가능.
        List<Item> itemList4 = query.select(item)
                .from(item)
                .where(qitem.itemName.like("..").and(qitem.price.loe(100)))
                .fetch();
        // 메서드로 각 조건 추가도 가능함. 코드재사용
        List<Item> itemList5 = query.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();


        return itemList4;
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
