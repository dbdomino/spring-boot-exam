package com.minod.itemservice.config;

import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfigV1 {

    private final DataSource dataSource; // 자동주입됨.

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }
    @Bean
    public ItemRepository itemRepository() {
//        return new JdbcTemplateItemRepositoryV2(dataSource);
        return new JdbcTemplateItemRepositoryV3(dataSource);
    }
}
