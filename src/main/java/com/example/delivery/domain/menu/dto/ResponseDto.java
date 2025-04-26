package com.example.delivery.domain.menu.dto;

import com.example.delivery.domain.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private Long id;
    private String name;
    private int price;
    private String description;

    // 생성 메서드 추가
    public static ResponseDto from(Menu menu) {
        return new ResponseDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription());
    }

}
