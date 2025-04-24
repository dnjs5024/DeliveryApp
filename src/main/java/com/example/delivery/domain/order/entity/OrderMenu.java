package com.example.delivery.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="order_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity; //수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="order_id")
    private Order order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name ="menu_id")
//    private Menu menu;

//    public OrderMenu(int quantity, Order order,Menu menu) {
//        this.quantity = quantity;
//        this.order = order;
//        this.menu = menu;
//    }
}
