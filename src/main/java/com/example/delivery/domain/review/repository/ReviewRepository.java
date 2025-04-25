package com.example.delivery.domain.review.repository;


import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {

//    void findAllByStoreId(Long storeId);

//        @Query("SELECT r FROM Review r where r.store.id = :storeId order by r.updatedAt desc")
//    public List<Review> findByStoreId(@Param("storeId") Long storeId);


    default Review findByIdOrElseThrow(Long reviewId){
        return findById(reviewId).orElseThrow(
            () -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }
}
