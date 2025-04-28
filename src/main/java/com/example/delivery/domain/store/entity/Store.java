package com.example.delivery.domain.store.entity;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private int minOrderPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status; // OPEN, CLOSED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User owner; // 사장님(User)

    // 추가
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    public void addMenu(Menu menu) {
        this.menus.add(menu);
        menu.setStore(this); //  양쪽 연관관계 설정
    }

     //생성자, 비즈니스 메서드 등 추가 예정
     //생성 메서드
    @Builder
    public Store(String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice, StoreStatus status, User owner) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.status = StoreStatus.OPEN;
        this.owner = owner;
    }

    public void validateOwner(Long ownerId) {
        if (!this.owner.getId().equals(ownerId)) {
            throw new CustomException(ErrorCode.ONLY_OWNER_MANAGE_STORE);
        }
    }

    public void update(String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;

    }

    public void changeStatus(StoreStatus newStatus) {
        this.status = newStatus;
    }

}