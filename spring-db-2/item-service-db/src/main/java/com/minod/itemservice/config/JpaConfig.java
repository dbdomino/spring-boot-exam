package com.minod.itemservice.config;

import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.jpa.JpaItemRepository;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;

//@Configuration
public class JpaConfig {

    private final EntityManager em; // 길어서 em으로 많이 쓴다고 함.
    public JpaConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

//    @Bean
    public ItemRepository itemRepository(){
        return new JpaItemRepository(em); // Repository는 Mybatis 에선 ItemMapper를 받고, JDBC 에선 DataSource를 받고, JPA에선 EntityManager 을 받는다.
        // JPA 모듈이 데이터 소스나 트랜잭션 매니저를 읽는다. 이후 자동으로 연결시켜 주므로, DataSource 별도호출 필요없다.
    }
}
