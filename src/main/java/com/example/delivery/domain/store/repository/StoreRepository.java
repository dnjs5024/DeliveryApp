package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 사장님이 운영중인 가게 개수
    int countByOwnerIdAndStatus(Long ownerId, StoreStatus status);
    // 사장님이 운영중인 가게 리스트
    List<Store> findAllByOwnerIdAndStatus(Long ownerId, StoreStatus status);

    // 가게 단건 조회 ( 폐업중인 가게는 조회 못함, 가게 단건 조회 시 그 가게의 메뉴도 보임 )
    // fetch join 사용해서 수정 예정
    //@Query("select s from Store s join fetch s.menu where s.id = :id and s.status =:status")
    Optional<Store> findWithMenuByIdAndStatus(Long StoreId, StoreStatus status);

    // 이름으로 검색, close 가게는 제외
    List<Store> findByNameContainingAndStatus(String keyword, StoreStatus status);

    // 페이지 기반 방식 전체 가게 조회
    Page<Store> findAllByStatus(StoreStatus status, Pageable pageable);

    // 커서 기반 페이징 방식은 offset 없이 id, createdAt 기준으로 그 이후 or 이전 데이터만 조회하는 방식
    // 커서 기반 페이징은 null 일때와 아닐 때를 나눠 쿼리해야한다.
    // 첫 페이지 요청은 커서없이
    // 내부 쿼리는 select * from store where status = ? order by id desc limit;
    List<Store> findTopByStatusOrderByIdDesc(StoreStatus status, Pageable pageable);
    //커서가 있는 경우
    List<Store> findTopByStatusAndIdLessThanOrderByIdDesc(StoreStatus status, Long lastId, Pageable pageable);

}
