package com.example.delivery.domain.menu.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.menu.dto.RequestDto;
import com.example.delivery.domain.menu.dto.ResponseDto;

import java.util.List;

public interface MenuService {
    ResponseDto create(Long loginUserId, RequestDto requestDto);
    ResponseDto update(Long loginUserId, Long menuId, RequestDto requestDto);
    void delete(Long loginUserId, Long menuId);

    List<ResponseDto> getMenusByStore(Store store);
}