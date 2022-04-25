//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.capstone.momomeal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "recommend_menu"
)
public class RecommendMenu {
    @Id
    @GeneratedValue
    @Column(
            name = "r_menu_id"
    )
    private Long r_menu_id;
    private String r_menu;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "menu_id"
    )
    private Menu menu;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id"
    )
    private Members member;

    public RecommendMenu() {
    }

    public Long getR_menu_id() {
        return this.r_menu_id;
    }

    public String getR_menu() {
        return this.r_menu;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Members getMember() {
        return this.member;
    }

    public void setR_menu_id(final Long r_menu_id) {
        this.r_menu_id = r_menu_id;
    }

    public void setR_menu(final String r_menu) {
        this.r_menu = r_menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public void setMember(final Members member) {
        this.member = member;
    }
}
