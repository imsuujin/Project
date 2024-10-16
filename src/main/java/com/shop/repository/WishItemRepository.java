package com.shop.repository;

import com.shop.entity.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {

    @Query("select w from WishItem w where w.member.email = :email")
    WishItem findByMemberEmail(String email);
}
