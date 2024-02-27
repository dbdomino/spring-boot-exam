package com.minod.itemservice;

import com.minod.itemservice.domain.Item;
import com.minod.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     * @EventListener(ApplicationReadyEvent.class) : 스프링 컨테이너가 완전히 초기화를 다 끝내고,
     * 실행 준비가 되었을 때 발생하는 이벤트이다. 스프링이 이 시점에 해당 애노테이션이 붙은 initData() 메서드를 호출해준다.
     *
     * 이 기능 대신 컨트롤러에서 @PostConstruct 를 사용할 경우 AOP 같은 부분이 아직 다 처리되지 않은 시점에
     * 호출될 수 있기 때문에, 간혹 문제가 발생할 수 있다. 예를 들어서 @Transactional 과 관련된 AOP가 적 용되지 않은 상태로 호출될 수 있다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
