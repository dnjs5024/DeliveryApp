package com.example.delivery.domain.review.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String content; // 리뷰내용

    @Column(nullable = false)
    private Integer rating; // 별점

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImage;

    private Review(Store store, User user, String content, Integer rating) {
        this.store = store;
        this.user = user;
        this.content = content;
        this.rating = rating;
    }

    @Builder
    public static Review of(Store store, User user, String content, Integer rating){
      return new Review(store, user, content, rating);
    }

}
