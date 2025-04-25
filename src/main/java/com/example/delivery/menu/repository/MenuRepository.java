package com.example.delivery.menu.repository;

import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;

public interface MenuRepository extends JpaRepository <Menu,Long> {

    default Menu findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(
                () -> new NotFoundException(ErrorCode.MENU_NOT_FOUND)
        );
    }
}
