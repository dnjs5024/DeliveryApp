package com.example.delivery.menu.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.review.entity.Review;
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
@SQLDelete(sql = "update menus SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private Menu(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isDeleted = false;
    }

    @Builder
    public static Menu of (String name, int price, String description){
        return new Menu(name, price, description);
    }

    public void update(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
