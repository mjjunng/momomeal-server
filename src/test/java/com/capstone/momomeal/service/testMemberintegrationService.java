
package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.MemberDTO;
import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.domain.RecommendCategory;
import com.capstone.momomeal.repository.MemberRepository;
import java.util.Iterator;
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
public class testMemberintegrationService {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    public testMemberintegrationService() {
    }
//
    @Test
    @Rollback(value = false)
    public void ck_updateUserInfo(){
        String email = "kimchang123@gmail.com";
        String RealName = "nothing";
        String img_url = "123123";

        Long a = Long.valueOf(1);
        Optional<Members> members = memberService.findById(a);
        Members member = members.get();
        memberService.updateUser(member.getUser_id(), member.getEmail(), member.getRealName(), img_url);
    }

    @Test
    public void ck_login(){
        String email = "kang889@test.com";
        String pwd = "test2";

        Optional<Members> members = memberService.Login(email,pwd);
        MemberDTO member = new MemberDTO();
        if(members.isEmpty()){
            Assertions.fail("로그인 실페");
        }else{
            member.setByMembers(members.get());
            System.out.println(member.toString());
            RecommendCategory rc = members.get().getRecommendCategory();
            try{
                rc.equals(null);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
//            System.out.println(rc.toString());
//            System.out.println("member = " + members.toString());
        }
    }
//
}
