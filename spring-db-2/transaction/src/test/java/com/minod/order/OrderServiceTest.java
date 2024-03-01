package com.minod.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// 트랜잭션 처리 실제 테스트, 서비스에 @Transactional 선언되어 있음.
@Slf4j
@SpringBootTest
class OrderServiceTest {
    @Autowired private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

//    @Test
    void empty() throws ExceptionNoMoney {

    }
    @Test
    void order() throws ExceptionNoMoney {
        Order order = new Order();
        //given
        order.setUsername("결제해줘요");

        //when
        orderService.order(order); // 체크드 예외라서 throws 선언 필요

        //then
        Order orderfind = orderRepository.findById(order.getId()).get(); // findById는 옵셔널로 출력함. 그래서 뒤에 get() 넣음.
        assertThat(orderfind.getPayStatus()).isEqualTo("승인");// 결제완료되면 payStatus가 "승인"로 될거임.
    }

    @Test
    void runtimeException() throws ExceptionNoMoney{
        //given
        Order order = new Order();
        order.setUsername("예외");

        //when
        Assertions.assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class); // 등록실패, 롤백

        //then 롤백되었으므로 데이터가 없어야 한다.
        Optional<Order> orderOptional = orderRepository.findById(order.getId());  // 데이터 jpa 가 제공하는 findById 메서드 기본메서드
        // 조회 실패, Optional 안에 null로 반환됨.
        // optional안에 Order가 없어야 한다. 롤백으로 db에 반영이 안되었는데, 그럼 order가 저장 안된거고, 조회도 id로 안될거다.
        assertThat(orderOptional.isEmpty()).isTrue();
        // Order findOrder = orderRepository.findById(order.getId()).get();  // 참고
    }

    @Test
    void biznessException() throws ExceptionNoMoney {
        //given
        Order order = new Order();
        order.setUsername("잔고부족");
        //when  체크드 예외로 try catch, 언체크드로 바꾸거나, 여기서 예외처리해버림. 여기선 후자!
        try {
            log.info("try 시작 하기.");
            orderService.order(order);
        } catch (ExceptionNoMoney e) {
            log.info("고객에게 잔고부족 안내 하기.");
//            throw new ExceptionNoMoney("롤백서비스.MyException() 커스텀");
            // 비즈니스 적으로 문제가 있는 주문이 db에 있어야 고객에게 보여줄 수 있다.
            // order 정보는 commit으로 저장된다.
        }

        //then
        Order findOrder = orderRepository.findById(order.getId()).get(); // 커밋되었으니 주문정보가 저장되었을 거다.
        log.info("findOrder 는 : {}",findOrder);
        assertThat(findOrder.getPayStatus()).isEqualTo("대기로 설정"); // 주문정보의 pay상태는 이게나올 것이다.
    }

}