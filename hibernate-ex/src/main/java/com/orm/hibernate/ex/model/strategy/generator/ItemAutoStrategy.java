package com.orm.hibernate.ex.model.strategy.generator;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;

//@Entity
public class ItemAutoStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает sequence hibernate_sequence
    protected Long id;

    public Long getId() {
        return id;
    }

    public static void main(String[] args) {
        EntitySaver.save(em -> {
            ItemAutoStrategy itemIdGenerator = new ItemAutoStrategy();
            em.persist(itemIdGenerator);
        });
    }
}
