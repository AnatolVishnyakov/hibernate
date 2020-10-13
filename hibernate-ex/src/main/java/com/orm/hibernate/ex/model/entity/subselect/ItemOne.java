package com.orm.hibernate.ex.model.entity.subselect;

import javax.persistence.*;

//@Entity
public class ItemOne {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    protected Long id;
    @Column
    private String name;
    @OneToOne
    private ItemTwo itemTwo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemTwo getItemTwo() {
        return itemTwo;
    }

    public void setItemTwo(ItemTwo itemTwo) {
        this.itemTwo = itemTwo;
    }
}
