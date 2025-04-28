package com.example.delivery.domain.image.entity;

import com.example.delivery.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetId; // Store.id, Menu.id, Review.id 중 하나

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String pKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageType type; // STORE, MENU,

    private Image(Long targetId, String imgUrl,ImageType type, String pKey) {
        this.targetId = targetId;
        this.imgUrl = imgUrl;
        this.type = type;
        this.pKey = pKey;
    }

    public static Image of(Long targetId, String imgUrl,ImageType type,String pKey){
        return new Image(targetId, imgUrl, type, pKey);
    }

    public void update(Long targetId, String imgUrl,ImageType type,String pKey) {
        this.targetId = targetId;
        this.imgUrl = imgUrl;
        this.type = type;
        this.pKey = pKey;
    }

}
