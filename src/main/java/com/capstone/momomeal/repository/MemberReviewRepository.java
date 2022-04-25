package com.capstone.momomeal.repository;

import com.capstone.momomeal.domain.MemberReview;
import java.util.List;
import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberReviewRepository {
    private final EntityManager em;

    public void saveReview(MemberReview memberReview) {
        this.em.persist(memberReview);
    }

    public List<MemberReview> reviewList(Long user_id) {
        List<MemberReview> reviews = this.em.createQuery("select m from MemberReview m where m.member.user_id = :user_id", MemberReview.class).setParameter("user_id", user_id).getResultList();
        return reviews;
    }
}
