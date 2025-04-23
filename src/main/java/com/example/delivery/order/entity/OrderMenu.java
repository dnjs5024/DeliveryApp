package com.example.delivery.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;

@Entity
@Table(name ="order_menu")
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

    public OrderMenu(){;}

    public OrderMenu(int quantity, Order order) {
        this.quantity = quantity;
        this.order = order;
    }
}
