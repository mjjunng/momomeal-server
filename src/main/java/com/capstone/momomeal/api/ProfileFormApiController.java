//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.MemberReview;
import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.domain.ReviewDto;
import com.capstone.momomeal.service.MemberReviewService;
import com.capstone.momomeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProfileFormApiController {

    private final MemberReviewService memberReviewService;
    private final MemberService memberService;

    @ResponseBody
    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    public HashMap<String, Object> getUserInfo(@RequestBody HashMap<String, Object> map){
        HashMap<String, Object> returnData = new HashMap<>();

        Long user_id = Long.valueOf((Integer)map.get("user_id"));

        Optional<Members> c_member = memberService.findById(user_id);
        if(c_member.equals(null)){
            returnData.put("check",0);
            return  returnData;
        }
        Members member = c_member.get();

        List<ReviewDto> reviewList = new ArrayList<>();

        for(MemberReview reviews : member.getMemberReview()){
            ReviewDto review = new ReviewDto(reviews);
            reviewList.add(review);
        }

        returnData.put("name",member.getRealName());
        returnData.put("email",member.getEmail());
        returnData.put("img_url",member.getImg());
        returnData.put("TotalRate",memberService.getUserTotalRate(member.getUser_id()));
        returnData.put("X_value",member.getX_value());
        returnData.put("Y_value",member.getY_value());
        returnData.put("reviewList",reviewList);

        return returnData;
    }

    @ResponseBody
    @RequestMapping(value = "updateUserInfo.do",method = RequestMethod.PUT)
    public HashMap<String, Object> updateUser(@RequestBody HashMap<String, Object> map){
        HashMap<String, Object> returnData = new HashMap<>();

        Long userId = Long.valueOf((Integer)map.get("user_id"));
        String email = (String)map.get("email");
        String RealName = (String)map.get("name");
        String ProfileImg = (String)map.get("profileImgUrl");
        map.clear();

        Boolean checkUpdate = memberService.updateUser(userId, email, RealName,ProfileImg);

        if(checkUpdate == true){
            returnData.put("check",1);
        }else{
            returnData.put("check",0);
        }
        return returnData;
    }

    @ResponseBody
    @RequestMapping(value = "curtUserInfo.do",method = RequestMethod.POST)
    public HashMap<String, Object> curtUserInfo(@RequestBody HashMap<String, Object> map){
        HashMap<String, Object> returnData = new HashMap<>();

        Long user_id = Long.valueOf((Integer)map.get("user_id"));

        Optional<Members> c_member = memberService.findById(user_id);
        if(c_member.equals(null)){
            returnData.put("check",0);
            return  returnData;
        }
        Members member = c_member.get();

        returnData.put("name",member.getRealName());
        returnData.put("img_url",member.getImg());

        return returnData;
    }

    @ResponseBody
    @RequestMapping(value = "setCoordinate.do",method = RequestMethod.PUT)
    public HashMap<String, Object> putXY(@RequestBody HashMap<String, Object> map){
        HashMap<String, Object> returnData = new HashMap<>();

        Long userId = Long.valueOf((Integer)map.get("user_id"));
        Double x_value = (Double)map.get("x");
        Double y_value = (Double)map.get("y");
        String address = (String)map.get("address");

        Boolean check = memberService.setCoordinate(userId, x_value, y_value, address);
        if(check == true){
            returnData.put("check",1);
        }else{
            returnData.put("check",0);
        }
        return returnData;
    }

}
