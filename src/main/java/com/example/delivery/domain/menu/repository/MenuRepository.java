package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.MENU_NOT_FOUND)
        );
    }


    // 특정 가게에서 삭제되지 않은 메뉴만 조회
    List<Menu> findAllByStoreAndDeletedFalse(Store store);
}
