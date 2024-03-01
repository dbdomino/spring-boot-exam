package com.minod.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// db에 입력이라, 컨트로러 없이 test로 테스트 구현

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // 트랜잭션 Commit 시점에 데이터 반영됨.
    @Transactional
    public void order(Order order) throws ExceptionNoMoney {
        log.info("order 메소드 서비스에서 호출");
        orderRepository.save(order);
        log.info("order 메소드 save() 완료"); // 이때 save 로 insert가 요청되지는 않는다. 트랜잭션 끝나는시점에 한꺼번에 수행된다.
        log.info("결제 시작"); //실제로 이 로그들이 먼저 수행되었음.
        if (order.getUsername().equals("예외")) {
            log.info("예외 상황 재현");
            throw new RuntimeException("시스템 예외");
        } else if (order.getUsername().equals("잔고부족")) {
            log.info("잔고 부족 예외 발생, 비즈니스 예외, 체크드로 커밋진행");
            order.setPayStatus("대기로 설정");
            throw new ExceptionNoMoney("잔고부족하오");
        } else {
            // 정상
            log.info("결제 승인요청");
            order.setPayStatus("승인"); // update 시 컬럼 하나만 수정되게 하는 방법은 없나? jpa에선?
            log.info("결제 승인완료");
        }
        log.info("order 메소드 완료");

    }
}
