package com.orm.hibernate.ex.model.entity.immutable;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.Immutable;

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
        QueryProcessor.process(entityManager -> {
            final Bid bid = new Bid();
            entityManager.persist(bid);
            bid.setName("test");
        });
    }
}
