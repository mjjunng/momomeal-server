package com.capstone.momomeal.domain;

import com.capstone.momomeal.domain.MemberReview;
import com.capstone.momomeal.domain.Status.RateStatus;
import lombok.Data;

@Data
public class ReviewDto {

    private Long review_id;
    private String review_text;
    private RateStatus rate;

    public ReviewDto(Long review_id, String review_text, RateStatus rate) {
        this.review_id = review_id;
        this.review_text = review_text;
        this.rate = rate;
    }

    public ReviewDto(){}

    public ReviewDto(MemberReview memberReview){
        this.review_id = memberReview.getReview_id();
        this.review_text = memberReview.getReview_text();
        this.rate = memberReview.getRate();
    }

    public void setByMemberReview(MemberReview memberReview) {
        this.review_id = memberReview.getReview_id();
        this.review_text = memberReview.getReview_text();
        this.rate = memberReview.getRate();
    }

    @Override
    public String toString() {
        return "ReviewDto{" +
                "review_id=" + review_id +
                ", review_text='" + review_text + '\'' +
                ", rate=" + rate +
                '}';
    }
}
