package com.example.delivery.menu.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.menu.dto.RequestDto;
import com.example.delivery.menu.dto.ResponseDto;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public ResponseDto create(RequestDto requestDto) {
        Store store = storeRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = Menu.of(store, requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        menuRepository.save(menu);

        return toDto(menu);
    }

    @Override
    @Transactional
    public ResponseDto update(Long id, RequestDto requestDto) {
        Menu menu = menuRepository.findByIdOrElseThrow(id);
        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        return toDto(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Menu menu = menuRepository.findByIdOrElseThrow(id);
        menu.delete();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDto> getMenusByStore(Store store) {
        return menuRepository.findByStoreDeleted(store).stream()
                .map(menu -> new ResponseDto(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getDescription()
                )).collect(Collectors.toList());
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
