package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //한 가게 주문 조회
    @EntityGraph(attributePaths = {"orderMenuList", "orderMenuList.menu"})
    Slice<Order> findByStoreIdAndOrderStatusNot(
            Long storeId,
            OrderStatus excludedStatus,
            Pageable pageable
    );


}
