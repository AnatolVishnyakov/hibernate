package com.orm.hibernate.ex.model.associations.onetomany.jointable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToMany(mappedBy = "buyer")
    private Set<Item> boughtItems = new HashSet<>();

    public User() {
    }

    public Set<Item> getBoughtItems() {
        return boughtItems;
    }

    public void setBoughtItems(Set<Item> boughtItems) {
        this.boughtItems = boughtItems;
    }
}
