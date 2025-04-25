package com.example.delivery.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private Long id;
    private String name;
    private int price;
    private String description;

}
