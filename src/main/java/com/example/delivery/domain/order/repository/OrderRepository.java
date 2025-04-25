package com.example.delivery.domain.order.repository;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //한 가게 주문 전체 조회(사장)
    @EntityGraph(attributePaths = {"orderMenuList", "orderMenuList.menu"})
    Slice<Order> findByStoreId(
            Long storeId,
            Pageable pageable
    );
//   (메뉴 정보까지 Fetch join)
    @EntityGraph(attributePaths = {"orderMenuList", "orderMenuList.menu"})
    Optional<Order> findByIdAndStoreId(Long orderId, Long storeId);

    default Order findByIdAndStoreIdOrElseThrow(Long orderId, Long storeId) {
        return findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

}
