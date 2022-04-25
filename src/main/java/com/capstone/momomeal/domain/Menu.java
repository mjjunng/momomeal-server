//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.capstone.momomeal.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue
    @Column(
            name = "menu_id"
    )
    private Long id;
    private String name;
    @OneToMany(
            mappedBy = "menu"
    )
    private List<RecommendMenu> recommendMenu = new ArrayList();

    public Menu() {
    }

    public void addRecommendMenu(RecommendMenu recommendMenu) {
        this.recommendMenu.add(recommendMenu);
        recommendMenu.setMenu(this);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<RecommendMenu> getRecommendMenu() {
        return this.recommendMenu;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRecommendMenu(final List<RecommendMenu> recommendMenu) {
        this.recommendMenu = recommendMenu;
    }
}
