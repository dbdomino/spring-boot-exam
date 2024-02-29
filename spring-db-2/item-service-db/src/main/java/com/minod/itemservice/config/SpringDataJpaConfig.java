package com.minod.itemservice.config;

import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.jpa.JpaItemRepositoryV2;
import com.minod.itemservice.repository.jpa.SpringDataJpaItemRepository;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {
    private final SpringDataJpaItemRepository springDataJpaItemRepository; // Interface 를 자동으로 AOP가 인스턴스생성시켜 준다만,
    // Data JPA 기준 기본제공 메소드로 만들어지다보니 ItemRepository의 인터페이스에서 제대로 동작 못함.
    // ItemRepository 인터페이스를 거쳐 수행하도록 리펙토링 해서 넣을 필요가 있는데, 그러기 위해 JpaItemRepositoryV2 수정필요.

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(springDataJpaItemRepository);
    }
}
