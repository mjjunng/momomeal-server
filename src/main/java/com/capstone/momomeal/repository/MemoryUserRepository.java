package com.capstone.momomeal.repository;

import com.capstone.momomeal.domain.MemberForm;
import com.capstone.momomeal.domain.Members;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryUserRepository {
    private static final Logger log = LoggerFactory.getLogger(MemoryUserRepository.class);
    static MemberForm memberForm = new MemberForm();

    public MemoryUserRepository() {
    }

    public void loginSuccess(Members member) {
        memberForm.setUser_id(member.getUser_id());
        memberForm.setUserEmail(member.getEmail());
        memberForm.setPwd(member.getPwd());
        memberForm.setUserName(member.getRealName());
        memberForm.setLogin(true);
    }

    public MemberForm getUser() {
        return memberForm;
    }

    public boolean checkLogin() {
        return memberForm.isLogin();
    }

    public void logOut(){
        memberForm.setUser_id(null);
        memberForm.setUserEmail(null);
        memberForm.setPwd(null);
        memberForm.setUserName(null);
        memberForm.setLogin(false);
    }

}
