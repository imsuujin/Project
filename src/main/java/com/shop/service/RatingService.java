package com.shop.service;

import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.RatingItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.RatingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RatingService {

    private final RatingItemRepository ratingItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private Item item;

    public void saveRating(RatingItem ratingItem) {
       float rating = ratingItem.getRating();
        ratingItemRepository.save(ratingItem);

        item.updateRating(rating);
    }

    public void deleteRating(String member_email) {
        Member member = memberRepository.findByEmail(member_email);
        RatingItem ratingItem = ratingItemRepository.findByMemberId(member.getId());
        log.info("ratingItem:" + ratingItem);
        item.deleteRating(ratingItem.getRating());
        ratingItemRepository.delete(ratingItem);
    }




}
