package com.orm.hibernate.ex.model.strategy.generator;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class ItemIdentityStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) // создает таблицу hibernate_sequence
    protected Long id;

    public Long getId() {
        return id;
    }

    public static void main(String[] args) {
        EntitySaver.save(em -> {
            ItemIdentityStrategy itemIdGenerator = new ItemIdentityStrategy();
            em.persist(itemIdGenerator);
        });
    }
}
