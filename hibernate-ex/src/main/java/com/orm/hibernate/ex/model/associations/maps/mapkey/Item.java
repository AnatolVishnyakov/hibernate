package com.orm.hibernate.ex.model.associations.maps.mapkey;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String name;
    @MapKey(name = "id")
    @OneToMany(mappedBy = "item")
    protected Map<Long, Bid> bids = new HashMap<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Long, Bid> getBids() {
        return bids;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
//            final Item item = new Item("test-item-" + new Random().nextInt(10_000));
//            entityManager.persist(item);
//
//            final Bid someBid = new Bid(BigDecimal.valueOf(new Random().nextDouble()), item);
//            entityManager.persist(someBid);
//
//            final Bid otherBid = new Bid(BigDecimal.valueOf(new Random().nextDouble()), item);
//            entityManager.persist(otherBid);

            final Item item = entityManager.find(Item.class, 485L);
            System.out.println("Number of bids: " + item.getBids().size());

            for (Map.Entry<Long, Bid> entry : item.getBids().entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        });
    }
}
