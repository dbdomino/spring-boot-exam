package com.minod.itemservice.config;

import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.memory.MemoryItemRepository;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;

//@Configuration
public class MemoryConfig {


    @Bean
    public ItemService itemService() {
        System.out.println("Memery Config test");
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
