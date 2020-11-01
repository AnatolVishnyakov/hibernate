package com.orm.hibernate.ex.model.entity.dynamic;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
//@DynamicInsert
//@DynamicUpdate
public class ItemDynamic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает sequence hibernate_sequence
    protected Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            entityManager.persist(new ItemDynamic());
        });
    }
}
