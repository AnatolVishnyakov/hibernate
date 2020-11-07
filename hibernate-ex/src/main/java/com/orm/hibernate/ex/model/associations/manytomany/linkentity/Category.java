package com.orm.hibernate.ex.model.associations.manytomany.linkentity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToMany(mappedBy = "category")
    private Set<CategorizedItem> categorizedItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Set<CategorizedItem> getCategorizedItems() {
        return categorizedItems;
    }
}
