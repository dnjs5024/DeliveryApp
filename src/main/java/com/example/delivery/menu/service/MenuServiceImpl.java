package com.example.delivery.menu.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.menu.dto.RequestDto;
import com.example.delivery.menu.dto.ResponseDto;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public ResponseDto create(RequestDto requestDto) {
        // StoreRepository가 아직 없으므로 임시 Store 객체로 연결
        Store fakeStore = new Store(requestDto.getStoreId()); // ← 기본 생성자 필요

        Menu menu = Menu.of(fakeStore, requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        menuRepository.save(menu);

        return toDto(menu);
    }

    @Override
    @Transactional
    public ResponseDto update(Long id, RequestDto requestDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        return toDto(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        menu.delete();
    }

    private ResponseDto toDto(Menu menu) {
        return new ResponseDto(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription()
        );
    }
}
