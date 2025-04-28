package com.example.delivery.domain.review.repository;


import com.example.delivery.common.exception.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("SELECT r FROM Review r where r.rating = :rating order by r.updatedAt desc")
    public List<Review> findByRating(@Param("rating") Long rating);


    default Review findByIdOrElseThrow(Long reviewId) {
        return findById(reviewId).orElseThrow(
            () -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    List<Review> findByUserId(Long userId);

    List<Review> findByStoreId(Long storeId);
}
