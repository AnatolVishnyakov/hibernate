package com.orm.hibernate.jta.model.filtering.dynamic;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String name;
    @OneToMany(mappedBy = "category")
    @org.hibernate.annotations.Filter(
            name = "limitByUserRank",
            condition =
                    ":currentUserRank >= (" +
                            "select u.RANK from USERS u " +
                            "where u.ID = SELLER_ID" +
                            ")"
    )
    protected Set<Item> items = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
