package com.orm.hibernate.ex.model.entity.immutable;

import com.orm.hibernate.ex.model.EntitySaver;
import com.orm.hibernate.ex.model.entity.dynamic.ItemDynamic;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
@Immutable
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает sequence hibernate_sequence
    protected Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final Bid bid = new Bid();
            entityManager.persist(bid);
            bid.setName("test");
        });
    }
}
