package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.delivery.domain.store.entity.Store;

import java.util.List;


public interface MenuRepository extends JpaRepository <Menu,Long> {

    default Menu findByIdOrElseThrow(Long menuId){
        return findById(menuId).orElseThrow(
                () -> new NotFoundException(ErrorCode.MENU_NOT_FOUND)
        );
    }
    // 이게 지금 실행하면 오류가 뜨거든요
    // findByStore =>  store.delete를 찾아서 그래요 근데 Store에는 delete가 없어요
    //List<Menu> findByStoreDeleted(Store store); // 이름만 보면 삭제된 메뉴를 가져오는 것 같아요
    //List<Menu> findAllByStoreAndDeletedFalse(Store store); <이것도 오류가 나서 JPQL로 작성
    @Query("SELECT m FROM Menu m WHERE m.store = :store AND m.deleted = false")
    List<Menu> findActiveMenusByStore(@Param("store") Store store);
}
