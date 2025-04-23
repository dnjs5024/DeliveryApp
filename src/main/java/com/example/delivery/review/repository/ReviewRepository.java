package com.example.delivery.review.repository;

import com.example.delivery.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long> {

//    void findAllByStoreId(Long storeId);

//        @Query("SELECT r FROM Review r where r.store.id = :storeId order by r.updatedAt desc")
//    public List<Review> findByStoreId(@Param("storeId") Long storeId);
}
