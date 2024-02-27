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
 * TransactionManager를 활용하여 스프링 AOP에서 제공하는 트랜잭션 기능 쓰자.
 * @Transactional로 트랜잭션 기능 주자.
 * 로직이 성공적으로 동작되면 commit 되도록 동작한다.
 * 그러나 Test에서 동작할 경우, 스프링은 테스트를 트랜잭션 안에서 실행하고, 테스트가 끝나면 트랜잭션을 자동으로 롤백 시켜 버린다.
 * 테스트는 테스트건마다 독립되어야 하므로, 자동 롤백을 지원해줌.
 */
@Slf4j
@Transactional
@SpringBootTest
class ItemRepositoryTransactionalTest {

    @Autowired ItemRepository itemRepository;
    //트랜잭션 관련 코드
    // DataSource, TransactionManager는 스프링이 자동주입 해준다. 그래서 프로퍼티에 설정만 해둬도 @Autowired로 주입 가능하다.
    // 스프링이 제공하는 트랜잭션 AOP는 스프링 빈에 등록된 트랜잭션 매니저를 찾아서 사용하기 때문에 트랜잭션 매니저를 스프링 빈으로 등록해두어야 한다.
/*    @Autowired PlatformTransactionManager transactionManager; // 트랜잭션 프록시에서 쓰기 위해 필요, 트랜잭션 매니저를 스프링 빈으로 등록한다.
    TransactionStatus status;

    @BeforeEach
    void beforeEach() {
    //트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition()); // 이거 Bean등록 안되어있으면 서비스에서 @Transactional 쓰더라도 롤백 지원 안됨.
        log.info("transaction start stasut : {}",status);
    }
    @AfterEach
    void afterEach() {
        // MemoryItemRepository 의 경우 제한적으로 사용.
        if (itemRepository instanceof MemoryItemRepository) {
            ((MemoryItemRepository) itemRepository).clearStore();
        } else {
            // 트랜잭션 롤백, 매우 핵심.
            transactionManager.rollback(status); // 테스트가 끝나면 롤백 수행되도록.
            log.info("transaction end stasut : {}",status);
        }
    }*/

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

        //둘 다 없음 검증
        test(null, null, item1, item2, item3);
        test("", null, item1, item2, item3);

        //itemName 검증
        test("itemA", null, item1, item2);
        test("temA", null, item1, item2);
        test("itemB", null, item3);

        //maxPrice 검증
        test(null, 10000, item1);

        //둘 다 있음 검증
        test("itemA", 10000, item1);
    }

    void test(String itemName, Integer maxPrice, Item... items) {
        List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));
        assertThat(result).containsExactly(items);
        // 메모리 리퍼지터리에서 비교기준은 객체주소로 해도 됬었다. 그러나 이제 db에서 끌어오는건 객체주소로 같을수가 없다. 테스트코드 새로필요
    }
}
