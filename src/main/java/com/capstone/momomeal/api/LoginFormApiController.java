package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.*;
import com.capstone.momomeal.repository.MemoryUserRepository;
import com.capstone.momomeal.service.MemberService;

import java.util.HashMap;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginFormApiController {
    private final MemberService memberService;
    private final MemoryUserRepository memoryUserRepository;

    @PostMapping({"createAccount.do"})
    public HashMap<String,Object> CreateAccount(@RequestBody HashMap<String, Object> map) {
        HashMap<String,Object> returnMap = new HashMap<>();

        MemberForm memberForm = new MemberForm();
        memberForm.setUserEmail((String)map.get("email"));
        memberForm.setPwd((String)map.get("pwd"));
        memberForm.setUserName((String)map.get("Rname"));

        Members member = new Members();
        member.setEmail((String)map.get("email"));
        member.setPwd((String)map.get("pwd"));
        member.setRealName((String)map.get("Rname"));
        member.setUser_rate(50F);

        try {
            memberService.join(member);
            returnMap.put("check",1);
            return returnMap;
        } catch (IllegalStateException var7) {
            throw new IllegalStateException("중복아이디 존재");
        }
    }

    @ResponseBody
    @PostMapping({"login.do"})
    public HashMap<String, Object> login(@RequestBody HashMap<String, Object> map) {
        HashMap<String,Object> returnMap = new HashMap<>();

        Members member = new Members();
        member.setEmail((String)map.get("email"));
        member.setPwd((String)map.get("pwd"));

        Optional<Members> members = memberService.Login(member.getEmail(), member.getPwd());
        LargerMemberDTO Smember = new LargerMemberDTO();

        if(members == null){
            returnMap.put("check",0);
            returnMap.put("member",null);
        }else{
            Members member_s = members.get();
            Smember.setByMembers(member_s);
            returnMap.put("check",1);
            returnMap.put("member",Smember);

            RecommendCategory rc = member_s.getRecommendCategory();
            if(rc == null){
                returnMap.put("recommend", "no");
            }else{
                returnMap.put("recommend", "yes");
            }
        }
        return returnMap;
    }

    @ResponseBody
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    public void logout(){
        memoryUserRepository.logOut();
    }
}
