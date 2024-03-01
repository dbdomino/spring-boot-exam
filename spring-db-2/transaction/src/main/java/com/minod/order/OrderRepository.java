package com.minod.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Crud 기본 만들기위해 데이터 JPA 사용.
}
