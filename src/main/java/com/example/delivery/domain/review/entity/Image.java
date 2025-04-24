package com.example.delivery.domain.review.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(nullable = false)
    private String imgUrl;

    @
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType type; // STORE, MENU,

    private Image(Review review, String imgUrl) {
        this.review = review;
        this.imgUrl = imgUrl;

    }

    public static Review of(Store store, User user, String content, Integer rating){
        return new Review(store, user, content, rating);
    }

    public void update(String content, Integer rating) {
        this.content = content;
        this.rating = rating;
    }

}
