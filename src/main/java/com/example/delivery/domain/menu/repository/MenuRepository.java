package com.example.delivery.domain.menu.repository;

import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.delivery.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository <Menu,Long> {

    default Menu findByIdOrElseThrow(Long menuId){
        return findById(menuId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MENU_NOT_FOUND)
        );
    }


}
