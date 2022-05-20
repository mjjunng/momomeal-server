//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.capstone.momomeal.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecommendCategory {
    @Id
    @GeneratedValue
    @Column(name = "recommend_category_id")
    private Long id;

    @OneToOne(mappedBy = "recommendCategory")
    private Members members;

    private int cntChicken;
    private int cntPizza;
    private int cntKorean;
    private int cntChinese;
    private int cntJapanese;
    private int cntWestern;
    private int cntSnackbar;
    private int cntMidnightSnack;
    private int cntBoiledPork;
    private int cntCafeAndDesert;
    private int cntFastfood;
    private int cnEtc;

    public RecommendCategory(Members members) {
        this.members = members;
        this.cntChicken = 0;
        this.cntPizza = 0;
        this.cntKorean = 0;
        this.cntChinese = 0;
        this.cntJapanese = 0;
        this.cntWestern = 0;
        this.cntSnackbar = 0;
        this.cntMidnightSnack = 0;
        this.cntBoiledPork = 0;
        this.cntCafeAndDesert = 0;
        this.cntFastfood = 0;
        this.cnEtc = 0;
    }

    /**
     * 점수가 높은 상위 n개의 카테고리 리턴하는 함수
     * @param n 상위 n개
     * @return 점수가 높은 상위 n개의 카테고리 담은 리스트
     */
    public List<Category> getTop3ScoreCategory(int n){
        Map<Category, Integer> map = new HashMap<Category, Integer>();
        List<Category> top3ScoreCategories = new ArrayList<>();

        map.put(Category.CHICKEN, this.cntChicken);
        map.put(Category.PIZZA, this.cntPizza);
        map.put(Category.KOREAN, this.cntKorean);
        map.put(Category.CHINESE, this.cntChinese);
        map.put(Category.JAPANESE, this.cntJapanese);
        map.put(Category.WESTERN, this.cntWestern);
        map.put(Category.SNACKBAR, this.cntSnackbar);
        map.put(Category.MIDNIGHTSNACK, this.cntMidnightSnack);
        map.put(Category.BOILEDPORK, this.cntBoiledPork);
        map.put(Category.CAFE, this.cntCafeAndDesert);
        map.put(Category.FASTFOOD, this.cntFastfood);
        map.put(Category.ETC, this.cnEtc);

        // value를 기준으로 내림차순 정렬
        List<Category> sortedScoreCategories = new ArrayList<>(map.keySet());
        Collections.sort(sortedScoreCategories, (o1, o2) -> (map.get(o2).compareTo(map.get(o1))));

        for (int i=0; i<n; i++) {
            top3ScoreCategories.add(sortedScoreCategories.get(i));
        }

        return top3ScoreCategories;
    }

    // 점수 리셋하는 함수
    public void initScore(){
        this.cntChicken = 0;
        this.cntPizza = 0;
        this.cntKorean = 0;
        this.cntChinese = 0;
        this.cntJapanese = 0;
        this.cntWestern = 0;
        this.cntSnackbar = 0;
        this.cntMidnightSnack = 0;
        this.cntBoiledPork = 0;
        this.cntCafeAndDesert = 0;
        this.cntFastfood = 0;
        this.cnEtc = 0;
    }
}
