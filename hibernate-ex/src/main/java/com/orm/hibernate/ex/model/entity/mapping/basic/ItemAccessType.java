package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;

//@Entity
public class ItemAccessType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Access(AccessType.PROPERTY)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            ItemAccessType item = new ItemAccessType();
            item.setName("test");
            entityManager.persist(item);
        });
    }
}
