package com.example.delivery.domain.menu.service;

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
    public ResponseDto create(Long loginUserId, RequestDto requestDto) {

//        Store store = storeRepository.findById(requestDto.getId())
//                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_FOUND));
        Store store = storeRepository.findMyStoreOrElseThrow(requestDto.getId(), loginUserId);
        // 메뉴 생성은 사장님만 가능하게 해야되는데 사장님 검증 로직이 빠짐

        Menu menu = Menu.of(store, requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());
        menuRepository.save(menu);

        return ResponseDto.from(menu);
    }

    @Override
    @Transactional
    public ResponseDto update(Long loginUserId, Long menuId, RequestDto requestDto) {
        // 사장님인지 검증 로직
        // 메뉴가 속한 가게의 소유자가 로그인된 유저인걸 검증하는 메서드
        storeRepository.findMyStoreOrElseThrow(requestDto.getId(), loginUserId);
        //메뉴 조회
        Menu menu = menuRepository.findByIdOrElseThrow(loginUserId);
        // 메뉴 업데이트
        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription());

        return ResponseDto.from(menu);
    }

    @Override
    @Transactional
    public void delete(Long loginUserId, Long menuId) {
        // 메뉴 조회
        Menu menu = menuRepository.findByIdOrElseThrow(loginUserId);
        // 사장인지 검증
        storeRepository.findMyStoreOrElseThrow(menu.getStore().getId(), loginUserId);
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
