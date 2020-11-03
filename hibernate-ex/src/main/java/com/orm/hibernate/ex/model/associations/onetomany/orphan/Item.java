package com.orm.hibernate.ex.model.associations.onetomany.orphan;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Arrays;
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
    @OneToMany(mappedBy = "item",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true) // Включает CascadeType.REMOVE
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
        });

        // Зависимые элементы сохраняются
        // автоматически
        QueryProcessor.process(entityManager -> {
            final Item itemUpdate = entityManager.createQuery("from Item i where i.id = " + item.id, Item.class)
                    .getSingleResult();
            itemUpdate.getBids().addAll(Arrays.asList(
//                    new Bid(1.0f, item),
//                    new Bid(3.0f, item),
//                    new Bid(5.0f, item),
                    new Bid(6.0f, item)
            ));

            entityManager.persist(itemUpdate);
        });

        QueryProcessor.process(entityManager -> {
            final Item buff = entityManager.find(Item.class, item.id);
            final Bid firstBid = buff.getBids().iterator().next();
            buff.getBids().remove(firstBid);
            System.out.println(buff.getBids().size());
        });
    }
}