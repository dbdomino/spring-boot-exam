package com.minod.itemservice.config;


import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.jpa.ItemQueryRepositoryV2;
import com.minod.itemservice.repository.jpa.ItemRepositoryV2;
import com.minod.itemservice.repository.jpa.JpaItemRepositoryV3;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 쿼리dsl용 리포지터리 + 데이터 jpa용 리포지터리 Bean등록 용 Config
 * 두개 같이 사용하는용 service 사용위해
 */

@Configuration
@RequiredArgsConstructor
public class DataJpaQueryDslConfig {

    private final EntityManager em; // jpa 사용위해 필요
    private final ItemRepositoryV2 itemRepositoryV2; // 스프링 데이터 JPA

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemQueryRepository(), itemRepositoryV2);
    }

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepository() {
        return new ItemQueryRepositoryV2(em);
    }

    @Bean // 기존에 JPA + Querydsl
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
