package com.shop.repository;

import com.shop.entity.RatingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingItemRepository extends JpaRepository<RatingItem, Long> {


    @Query("select r from RatingItem r WHERE r.member = :memberId")
    RatingItem findByMemberId(Long memberId);
}
