package com.example.delivery.menu.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.menu.dto.RequestDto;
import com.example.delivery.menu.dto.ResponseDto;

import java.util.List;

public interface MenuService {
    ResponseDto create(RequestDto requestDto);
    ResponseDto update(Long id, RequestDto requestDto);
    void delete(Long id);

    List<ResponseDto> getMenusByStore(Store store);
}