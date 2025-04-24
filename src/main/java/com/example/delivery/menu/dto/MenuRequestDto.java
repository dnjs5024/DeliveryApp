package com.example.delivery.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String name;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    private String description;

    public MenuRequestDto(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
