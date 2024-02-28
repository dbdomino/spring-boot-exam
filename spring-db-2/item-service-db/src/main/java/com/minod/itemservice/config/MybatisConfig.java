package com.minod.itemservice.config;

import com.minod.itemservice.repository.ItemRepository;
import com.minod.itemservice.repository.mybatis.ItemMapper;
import com.minod.itemservice.repository.mybatis.MyBatisItemRepository;
import com.minod.itemservice.service.ItemService;
import com.minod.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

//@Configuration
@RequiredArgsConstructor
public class MybatisConfig {

    private final ItemMapper itemMapper; // 이걸 잘 못읽어드리는것 같다.

/*    @Autowired
    public MybatisConfig(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }*/

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository(){
        return new MyBatisItemRepository(itemMapper); // Repository는 이제 DataSource를 받지않고, ItemMapper를 받는다.
        // mybatis 모듈이 데이터 소스나 트랜잭션 매니저를 읽는다. 이후 Mapper와 자동으로 연결시켜 주므로, DataSource 별도호출 필요없다.
    }

}
