package com.orm.hibernate.ex.model.associations.onetomany.bag;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToMany(mappedBy = "item")
    private Collection<Bid> bids = new ArrayList<>();

    public Item() {
    }

    public Collection<Bid> getBids() {
        return bids;
    }

    public void setBids(Collection<Bid> bids) {
        this.bids = bids;
    }

    public static void main(String[] args) {
        final Item item = new Item();
        QueryProcessor.process(entityManager -> {
            entityManager.persist(item);
            final Bid bid = new Bid(new BigDecimal("1.4"), item);
            item.getBids().add(bid);
            item.getBids().add(bid); // Hibernate проигнорирует туже самую ссылку
            entityManager.persist(bid);
        });

//        QueryProcessor.process(entityManager -> {
//            final Item it = entityManager.find(Item.class, item.id);
//            System.out.println(it.getBids());
//        });

        QueryProcessor.process(entityManager -> {
//            final Item item2 = entityManager.find(Item.class, item.id);
            final Item item2 = entityManager.getReference(Item.class, item.id);
            final Bid bid = new Bid(new BigDecimal(String.valueOf(new Random().nextDouble())), item2);
            item2.getBids().add(bid);
            final Bid bid1 = new Bid(new BigDecimal(String.valueOf(new Random().nextDouble())), item2);
            item2.getBids().add(bid1);
            entityManager.persist(bid);
            entityManager.persist(bid1);
        });
    }
}
