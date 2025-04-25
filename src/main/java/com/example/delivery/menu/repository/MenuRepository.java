package com.example.delivery.menu.repository;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.MENU_NOT_FOUND)
        );
    }
    List<Menu> findByStoreDeleted(Store store);
}
