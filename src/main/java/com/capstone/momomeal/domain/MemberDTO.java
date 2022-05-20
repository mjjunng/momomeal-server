package com.capstone.momomeal.domain;

import lombok.Data;

@Data
public class MemberDTO {
    Long userId;
    String email;
    String pwd;
    String name;
    String img_url;

    public MemberDTO(Long userId, String email, String pwd, String name, String img_url) {
        this.userId = userId;
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.img_url = img_url;
    }

    public MemberDTO(){}
    public void setByMembers(Members members){
        this.userId = members.getUser_id();
        this.email = members.getEmail();
        this.pwd = members.getPwd();
        this.name = members.getRealName();
        this.img_url = members.getImg();
    }
}
