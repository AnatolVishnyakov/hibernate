package com.orm.hibernate.ex.model.associations.onetomany;

import javax.persistence.*;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;

    public Item(String name) {
        this.name = name;
    }
}