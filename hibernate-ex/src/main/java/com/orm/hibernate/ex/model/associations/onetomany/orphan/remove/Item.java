package com.orm.hibernate.ex.model.associations.onetomany.orphan.remove;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    protected String name;
    @OneToMany(mappedBy = "item",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true) // Включает CascadeType.REMOVE
    protected Set<Bid> bids = new HashSet<>();

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

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public static void main(String[] args) {
        User johndoe = new User();
        Item anItem = new Item("Some Item");
        QueryProcessor.process(em -> {
            em.persist(johndoe);

            Bid bidA = new Bid(new BigDecimal("123.00"), anItem);
            bidA.setBidder(johndoe);
            anItem.getBids().add(bidA);

            Bid bidB = new Bid(new BigDecimal("456.00"), anItem);
            anItem.getBids().add(bidB);
            bidB.setBidder(johndoe);

            em.persist(anItem);
        });

        QueryProcessor.process(em -> {
            User user = em.find(User.class, johndoe.getId());
            System.out.println("Number of bids before remove: " + user.getBids().size());

            Item item = em.find(Item.class, anItem.getId());
            Bid firstBid = item.getBids().iterator().next();
            item.getBids().remove(firstBid);

            // FAILURE!
            // assertEquals(user.getBids().size(), 1);
            System.out.println("Number of bids after remove: " + user.getBids().size());
        });
    }
}
