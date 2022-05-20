package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.MemberReview;
import com.capstone.momomeal.repository.MemberReviewRepository;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReviewService {
    private final MemberReviewRepository memberReviewRepository;

    @Transactional(
            readOnly = true
    )
    public List<MemberReview> getReviewList(Long user_id) {
        List<MemberReview> reviews = this.memberReviewRepository.reviewList(user_id);
        return reviews;
    }

    public void addReview(MemberReview memberReview) {
        memberReviewRepository.saveReview(memberReview);
    }
}
