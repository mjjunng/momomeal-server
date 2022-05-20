package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.MemberReview;
import com.capstone.momomeal.domain.ReviewDto;
import com.capstone.momomeal.domain.Status.RateStatus;
import com.capstone.momomeal.service.MemberReviewService;
import com.capstone.momomeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewFormApiController {

    private final MemberReviewService memberReviewService;
    private final MemberService memberService;

    @ResponseBody
    @RequestMapping(value = "addReview.do", method = RequestMethod.PUT)
    public HashMap<String, Object> addReview(@RequestBody HashMap<String,Object> map){
        HashMap<String, Object> returnMap = new HashMap<>();

        Long user_id = Long.valueOf((Integer)map.get("user_id"));
        String review = (String)map.get("review");
        RateStatus rateStatus = ((String)map.get("rate")).equals("LIKE") ? RateStatus.LIKE : RateStatus.BAD;

        MemberReview memberReview = new MemberReview();
        memberReview.setReview_text(review);
        memberReview.setMember(memberService.findById(user_id).get());
        memberReview.setRate(rateStatus);

        memberReviewService.addReview(memberReview);

        returnMap.put("check",1);
        return returnMap;
    }

    @ResponseBody
    @RequestMapping(value = "getReviewList.do", method = RequestMethod.GET)
    public HashMap<String, Object> getReviewList(@RequestBody HashMap<String, Object> map){
        HashMap<String, Object> returnMap = new HashMap<>();

        Long user_id = Long.valueOf((Integer)map.get("user_id"));

        List<MemberReview> reviewList_s = memberReviewService.getReviewList(user_id);

        List<ReviewDto> reviewList = new ArrayList<>();

        for(MemberReview review: reviewList_s){
            ReviewDto reviewDto = new ReviewDto(review);
            reviewList.add(reviewDto);
        }
        returnMap.put("reviewList",reviewList);

        return returnMap;
    }
}
