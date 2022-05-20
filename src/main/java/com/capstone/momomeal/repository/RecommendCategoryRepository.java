package com.capstone.momomeal.repository;

import com.capstone.momomeal.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendCategoryRepository {

    private final EntityManager em;

    public void save(RecommendCategory recommendCategory){
        em.persist(recommendCategory);
    }


}
