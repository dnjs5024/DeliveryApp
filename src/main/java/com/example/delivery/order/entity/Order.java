package com.example.delivery.order.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sotre_id")
//    private Store store;

    //읽기전용 OrderMenu가 주인
    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<OrderMenu> orderMenuList;

    //생성자
    private Order(OrderStatus orderStatus, List<OrderMenu> orderMenuList) {
        this.orderStatus = orderStatus;
        this.orderMenuList = orderMenuList;
    }
    //팩토리 정적 메소드
    public static Order create(OrderStatus status) {
        return new Order(status, new ArrayList<>());
    }
    //수정 메소드
    public void changeStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }
}
