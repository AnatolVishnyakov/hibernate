package com.orm.hibernate.ex.model.associations.onetomany.list.bidirectional;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToMany
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false
    )
    @OrderColumn(
            name = "BID_POSITION",
            nullable = false
    )
    private List<Bid> bids = new ArrayList<>();

    public List<Bid> getBids() {
        return bids;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            entityManager.persist(item);
            for (int i = 0; i < new Random().nextInt(10); i++) {
                final Bid bid = new Bid(new BigDecimal(String.valueOf(new Random().nextDouble())), item);
                item.getBids().add(bid);
                entityManager.persist(bid);
            }
        });
    }
}
