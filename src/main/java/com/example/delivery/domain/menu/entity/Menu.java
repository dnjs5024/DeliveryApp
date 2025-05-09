package com.example.delivery.domain.menu.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "menus")
@Getter
@SQLDelete(sql = "update menus SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean deleted = false;

    public void setStore(Store store) {
        this.store = store;
    }

    private Menu(Store store, String name, int price, String description) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.description = description;
        this.deleted = false;
    }

    @Builder
    public static Menu of (Store store, String name, int price, String description){
        return new Menu(store, name, price, description);
    }

    public void update(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void delete() {
        this.deleted = true;
    }
}