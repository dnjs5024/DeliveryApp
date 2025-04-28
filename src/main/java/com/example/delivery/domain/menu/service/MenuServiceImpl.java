package com.example.delivery.domain.menu.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.menu.dto.RequestDto;
import com.example.delivery.domain.menu.dto.ResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
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
    public ResponseDto create(Long ownerId, RequestDto requestDto) {

        // 1. 가게 조회
        Store store = storeRepository.findByIdOrElseThrow(requestDto.getId());

        // 2. 가게 오너 검증
        store.validateOwner(ownerId);

        //Store store = storeRepository.findMyStoreOrElseThrow(requestDto.getId(), loginUserId);
        // 메뉴 생성은 사장님만 가능하게 해야되는데 사장님 검증 로직이 빠짐 = > 문제점 : repository가 조회 + 검증을 함, 즉 비즈니스 로직을 처리함

        Menu menu = Menu.of(store, requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        menuRepository.save(menu);

        return ResponseDto.from(menu);
    }

    @Override
    @Transactional
    public ResponseDto update(Long ownerId, Long menuId, RequestDto requestDto) {
        // 사장님인지 검증 로직
        // 메뉴가 속한 가게의 소유자가 로그인된 유저인걸 검증하는 메서드
       // storeRepository.findMyStoreOrElseThrow(requestDto.getId(),ownerId);
        // 1. 메뉴 조회
        Menu menu = menuRepository.findByIdOrElseThrow(ownerId);
        // 2. 스토어 오너 검증
        Store store = menu.getStore();
        store.validateOwner(ownerId);

        // 메뉴 업데이트
        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());

        return ResponseDto.from(menu);
    }

    @Override
    @Transactional
    public void delete(Long ownerId, Long menuId) {
        // 메뉴 조회
        Menu menu = menuRepository.findByIdOrElseThrow(ownerId);
        // 사장인지 검증
        // storeRepository.findMyStoreOrElseThrow(menu.getStore().getId(), ownerId);
        Store store = menu.getStore();
        store.validateOwner(ownerId);
        menu.delete();
    }
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDto> getMenusByStore(Store store) {
        return menuRepository.findActiveMenusByStore(store).stream()
                .map(ResponseDto::from)
                .collect(Collectors.toList());

    }
}
