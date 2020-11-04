package com.orm.hibernate.ex.model.associations.onetomany.list.onedirectional;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
//        QueryProcessor.process(entityManager -> {
//            final Item item = new Item();
//            entityManager.persist(item);
//            for (int i = 0; i < new Random().nextInt(10); i++) {
//                final Bid bid = new Bid(new BigDecimal(String.valueOf(new Random().nextDouble())));
//                item.getBids().add(bid);
//                entityManager.persist(bid);
//            }
//        });

        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, 398L);
            item.getBids().forEach(System.out::println);
        });
    }
}
