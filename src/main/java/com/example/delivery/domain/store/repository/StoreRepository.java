package com.example.delivery.domain.store.repository;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영중인 가게 개수
    int countByOwnerIdAndStatus(Long ownerId, StoreStatus status);

    // 사장님이 운영중인 가게 리스트
    List<Store> findAllByOwnerIdAndStatus(Long ownerId, StoreStatus status);

    // 상태 상관 없이 모든 가게 조회 리스트
    List<Store> findAllByOwnerId(Long ownerId);

    Optional<Store> findByIdAndOwnerId(Long id, Long ownerId);

    // 특정 가게 단건 조회 ( 폐업중인 가게는 조회 못함, 가게 단건 조회 시 그 가게의 메뉴도 보임 )
    @Query("select s from Store s join fetch s.menus m where s.id = :id and s.status = :status")
    Optional<Store> findWithMenuByIdAndStatus(@Param("id")Long id, @Param("status") StoreStatus status);

    // 이름으로 검색하여 폐업 제외한 가게 리스트 조회 (페이지네이션)
    Page<Store> findByNameContainingAndStatus(String keyword, StoreStatus status, Pageable pageable);

    // 상태 기준으로 모든 가게 조회 (페이지네이션)
    Page<Store> findAllByStatus(StoreStatus status, Pageable pageable);


    // 커서 기반 페이징 : 상태별 조회, 첫 페이지 요청 시 커서 없이
    // 내부 쿼리는 select * from store where status = ? order by id desc limit;
    List<Store> findTopByStatusOrderByIdDesc(StoreStatus status, Pageable pageable);

    // 커서 기반 페이징: 상태별 조회, 특정 ID 이하의 데이터 조회
    List<Store> findTopByStatusAndIdLessThanOrderByIdDesc(StoreStatus status, Long lastId, Pageable pageable);

    default Store findByIdOrElseThrow(Long storeId) {
        return findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

}
