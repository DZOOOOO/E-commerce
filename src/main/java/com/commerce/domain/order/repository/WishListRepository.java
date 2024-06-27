package com.commerce.domain.order.repository;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.order.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByProductId(Long productId);

    Optional<WishList> findByMember(Member member);

    void deleteByIdAndMember(Long wishListId, Member member);

    Optional<WishList> findByMemberAndProductId(Member member, Long productId);
}
