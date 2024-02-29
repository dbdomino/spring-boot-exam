package com.minod.itemservice.repository.jpa;

import com.minod.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 스프링 데이터 JPA는 인터페이스로 만든다.
// 데이터 jpa를 쓴다는건 Entity 저장소를 관리한다는 소리라, extends 뒤에 제네릭설정 해줘야함.<entity객체,PK type>
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    // JpaRepository 인터페이스의 기본적인 기능을 쓸 수 있다.

    // CRUD는 인터페이스를 보고 AOP 프록시에서 자동으로 구현체를 만들어 준다고 한다.
    // Create, Read는 PK기준 조회, Update는 setter기준, Delete는 PK기준
    // 다만 PK기준 말고 다른 컬럼으로 조회하는 기능은 공통제공이 안된다. (쿼리 메소드/@Query("") 로 직접구현 해야한다.)
    // 공통 인터페이스가 제공하는 기능 findAll() // select i from Item i

    // 아래의 쿼리메서드는 자동완성 기능도 지원되는데, 자동완성 안뜨면 설정 추가 해줘야 함.
    List<Item> findByItemNameLike(String itemName); // select i from Item i where i.name like ?
    List<Item> findByPriceLessThanEqual(Integer price); // select i from Item i where i.price <= ?

    // 쿼리 메서드 길고 복잡하고 알기어렵게 하하, 아는사람만 알게하기 (select i from Item i where i.itemName like ? and i.price <= ?)
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);
    // JPQL 쿼리로 넣으면 이렇다.
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);

    // 조건에 따라 쿼리가 추가되는 동적 쿼리는 JPA로 구현이 어렵다.(자동으로 쿼리만들어 주면서 이기능은 구현이 안된듯함. 이는 QueryDSL로 해결해야 함.)
}
