package com.capstone.momomeal.domain;

import lombok.Data;

@Data
public class LargerMemberDTO {
    Long userId;
    String email;
    String pwd;
    String name;
    String img_url;
    Double x;
    Double y;
    String address;


    public LargerMemberDTO(Long userId, String email, String pwd, String name, String img_url, Double x, Double y, String address) {
        this.userId = userId;
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.img_url = img_url;
        this.x = x;
        this.y = y;
        this.address = address;
    }
    public LargerMemberDTO(){}

    public void setByMembers(Members members){
        this.userId = members.getUser_id();
        this.email = members.getEmail();
        this.pwd = members.getPwd();
        this.name = members.getRealName();
        this.img_url = members.getImg();
        this.x = members.getX_value();
        this.y = members.getY_value();
        this.address = members.getAddress();
    }
}
