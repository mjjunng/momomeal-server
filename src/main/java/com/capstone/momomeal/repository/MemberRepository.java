package com.capstone.momomeal.repository;

import com.capstone.momomeal.domain.MemberDTO;
import com.capstone.momomeal.domain.Members;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(MemberRepository.class);
    @PersistenceContext
    private EntityManager em;

    public MemberRepository() {
    }

    public void save(Members member) {
        this.em.persist(member);
    }

    public Optional<MemberDTO> getInfo(Long userId){
        List<MemberDTO> members = em.createQuery("select new com.capstone.momomeal.domain." +
                "MemberDTO(m.user_id, m.email, m.pwd, m.realName, m.img_url)" +
                " from Members m" +
                " where m.user_id = :userId", MemberDTO.class)
                .setParameter("userId",userId)
                .getResultList();
        Optional<MemberDTO> member = Optional.ofNullable(members.get(0));
        return member;
    }

    public Optional<Members> findOne(Long user_id) {
        Members member = em.find(Members.class, user_id);
        return Optional.ofNullable(member);
    }

    public Members findById(Long user_id){
        Members member = em.find(Members.class,user_id);
        return member;
    }

    public Optional<Members> findEmail(String email) {
        List<Members> result = em.createQuery("select m from Members m where m.email = :email", Members.class).setParameter("email", email).getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            Optional<Members> member = Optional.ofNullable((Members)result.get(0));
            return member;
        }
    }

    public Optional<Members> findIdAndPwd(String email, String pwd) {
        List<Members> result = em.createQuery("select m from Members m where m.pwd = :pwd AND m.email = :email", Members.class).setParameter("pwd", pwd).setParameter("email", email).getResultList();
        return result.stream().findAny();
    }

    public List<Members> findAll() {
        List<Members> result = em.createQuery("select m from Members m", Members.class).getResultList();
        return result;
    }
}
