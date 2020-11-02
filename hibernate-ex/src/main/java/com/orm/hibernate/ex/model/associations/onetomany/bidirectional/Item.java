package com.orm.hibernate.ex.model.associations.onetomany.bidirectional;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<Bid> bids = new HashSet<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Item item = new Item("test-" + new Random().nextInt(10_000));
        QueryProcessor.process(entityManager -> {
            entityManager.persist(item);
            entityManager.persist(new Bid(1.0f, item));
            entityManager.persist(new Bid(3.0f, item));
            entityManager.persist(new Bid(5.0f, item));
            entityManager.persist(new Bid(6.0f, item));
        });

        QueryProcessor.process(entityManager -> {
            final Item result = entityManager.createQuery("from Item i where i.id = " + item.id, Item.class)
                    .getSingleResult();
            System.out.println(result);
            result.getBids().forEach(System.out::println);
        });
    }
}