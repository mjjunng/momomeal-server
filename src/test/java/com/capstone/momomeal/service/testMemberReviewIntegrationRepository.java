//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.MemberForm;
import com.capstone.momomeal.domain.MemberReview;
import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.domain.ReviewDto;
import com.capstone.momomeal.domain.Status.RateStatus;
import com.capstone.momomeal.repository.MemberReviewRepository;
import com.capstone.momomeal.repository.MemoryUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class testMemberReviewIntegrationRepository {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberReviewRepository memberReviewRepository;
    @Autowired
    MemoryUserRepository memoryUserRepository;
    @Autowired
    MemberReviewService memberReviewService;

    public testMemberReviewIntegrationRepository() {
    }
//
    @Test
    @Rollback(value = false)
    public void addReview() {
        Optional<Members> members = memberService.Login("test1@test.com", "test1");
        Members member1 = members.get();

        MemberReview memberReview1 = new MemberReview();
        memberReview1.setMember(member1);
        memberReview1.setReview_text("그냥 그래요");
        memberReview1.setRate(RateStatus.BAD);

        MemberReview memberReview2 = new MemberReview();
        memberReview2.setMember(member1);
        memberReview2.setReview_text("머지 이건");
        memberReview2.setRate(RateStatus.BAD);

        MemberReview memberReview3 = new MemberReview();
        memberReview3.setMember(member1);
        memberReview3.setReview_text("우오오오");
        memberReview3.setRate(RateStatus.LIKE);

        MemberReview memberReview4 = new MemberReview();
        memberReview4.setMember(member1);
        memberReview4.setReview_text("으으윽");
        memberReview4.setRate(RateStatus.BAD);

        memoryUserRepository.loginSuccess(member1);
        MemberForm memberForm = this.memoryUserRepository.getUser();

        if (memberForm.isLogin()) {
            memberReviewService.addReview(memberReview1);
            memberReviewService.addReview(memberReview2);
            memberReviewService.addReview(memberReview3);
            memberReviewService.addReview(memberReview4);
        }

    }
//
    @Test
    public void getReviewList() {
        Optional<Members> members = memberService.findById(1L);
        if(members.equals(null)){
            Assertions.fail("없는 번호");
        }
        Members member = members.get();

        List<MemberReview> reviewList_s = memberReviewService.getReviewList(member.getUser_id());
        List<ReviewDto> reviewList = new ArrayList<>();
        for(MemberReview review : reviewList_s){
            ReviewDto reviewDto = new ReviewDto(review);
            reviewList.add(reviewDto);
        }
        for(ReviewDto review : reviewList){
            System.out.println("review = " + review);
        }
    }

}
