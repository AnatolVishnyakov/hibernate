package com.orm.hibernate.ex.model.strategy.generator;

import javax.persistence.*;

@Entity
public class ItemAutoStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // using hibernate_sequence
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
