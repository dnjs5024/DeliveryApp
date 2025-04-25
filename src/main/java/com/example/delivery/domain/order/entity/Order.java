package com.example.delivery.domain.order.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name ="orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 500) //요청 사항
    private String requestMessage;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    //읽기전용 OrderMenu가 주인
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    public static Order create(User user,
                               Store store,
                               List<OrderMenu> menus,
                               String requestMessage) {
        Order order = new Order();
        order.user = user;
        order.store = store;
        order.requestMessage = requestMessage;
        order.orderStatus = OrderStatus.PENDING;
        menus.forEach(order::addOrderMenu);
        order.calculateTotalPrice(); // 총 가격 메소드 실행(필드 채우기)
        return order;
    }

    //OrderMenu 하나씩 넣기
    public void addOrderMenu(OrderMenu menu) {
        menu.setOrder(this);
        this.orderMenuList.add(menu);
    }

    // 메뉴 주문 총 가격
    public void calculateTotalPrice() {
        this.totalPrice = orderMenuList.stream()
            .map(OrderMenu::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //상태 수정 메소드
    public void changeStatus(OrderStatus newStatus){
        this.orderStatus = newStatus;
    }
}

