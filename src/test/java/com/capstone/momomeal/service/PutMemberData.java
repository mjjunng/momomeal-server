package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PutMemberData {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void PutData(){
        Members members1 = new Members();
        Members members2 = new Members();
        Members members3 = new Members();
        Members members4 = new Members();
        Members members5 = new Members();

        members1.setEmail("test1@test.com");
        members1.setPwd("test1");
        members1.setRealName("kim");

        members2.setEmail("kang889@test.com");
        members2.setPwd("test2");
        members2.setRealName("kang");

        members3.setEmail("hwangUk3@test.com");
        members3.setPwd("1234");
        members3.setRealName("hwang");

        members4.setEmail("ParkJy@test.com");
        members4.setPwd("9876");
        members4.setRealName("park");

        members5.setEmail("songwooJin@test.com");
        members5.setPwd("5678");
        members5.setRealName("Song");

        memberService.join(members1);
        memberService.join(members2);
        memberService.join(members3);
        memberService.join(members4);
        memberService.join(members5);
    }

}
