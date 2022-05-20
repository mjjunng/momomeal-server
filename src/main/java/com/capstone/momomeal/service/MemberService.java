package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.MemberDTO;
import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.repository.MemberRepository;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    public Optional<Members> findById(Long user_id){
        return memberRepository.findOne(user_id);
    }

    public String join(Members member) {
        log.info("member email = {}", member.getEmail());
        log.info("member = {}", this.memberRepository.findEmail(member.getEmail()));
        if (this.memberRepository.findEmail(member.getEmail()) == null) {
            this.memberRepository.save(member);
            return member.getEmail();
        } else {
            throw new IllegalStateException("중복 아이디 존재");
        }
    }

    public Boolean updateUser(Long userId, String email, String RealName, String img){
        try {
            Optional<Members> member_s = memberRepository.findOne(userId);
            if(member_s == null){
                return false;
            }
            else {
                Members member = member_s.get();
                member.setEmail(email);
                member.setRealName(RealName);
                member.setImg(img);
                return true;
            }
        }catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<Members> findAll() {
        return this.memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Members> findEmail(String email) {
        Optional<Members> result = this.memberRepository.findEmail(email);
        if (result != null) {
            throw new IllegalStateException("중복 존재");
        } else {
            return result;
        }
    }

    @Transactional(readOnly = true)
    public Optional<Members> Login(String email, String pwd) {
        return this.memberRepository.findIdAndPwd(email, pwd);
    }

    public Boolean updateUser(Long userId, String email, String RealName){
        try {
            Members member = memberRepository.findById(userId);

            member.setEmail(email);
            member.setRealName(RealName);
//            member.setImg(img);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public Boolean setCoordinate(Long userId, Double x, Double y, String address){
        try {
            Members member = memberRepository.findById(userId);

            member.setX_value(x);
            member.setY_value(y);
            member.setAddress(address);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Integer getMember_rate(Long user_id){
        Optional<Members> members = memberRepository.findOne(user_id);
        if(members.equals(null)){
            return null;
        }
        Members member = members.get();
        Integer returnRate = Integer.valueOf(member.TotalRate());

        return returnRate;
    }

    public Integer getUserTotalRate(Long user_id){
        Optional<Members> one = memberRepository.findOne(user_id);

        if(one.equals(null)){
            return null;
        }
        Members member = one.get();
        return member.TotalRate();
    }
}
