package com.orm.hibernate.ex.model.associations.manytomany.linkentity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToMany(mappedBy = "item")
    private Set<CategorizedItem> categorizedItems = new HashSet<>();
    @Column
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<CategorizedItem> getCategorizedItems() {
        return categorizedItems;
    }
}
