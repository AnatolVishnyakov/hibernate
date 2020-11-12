package com.orm.hibernate.ex.model.complexschemas.custom;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;

    protected String name;

    @NotNull
    protected Date auctionStart;

    @NotNull
    protected Date auctionEnd;

    @OneToMany(mappedBy = "item")
    protected Set<Bid> bids = new HashSet<>();

    public Item() {
    }

    public Item(String name, Date auctionStart, Date auctionEnd) {
        this.name = name;
        this.auctionStart = auctionStart;
        this.auctionEnd = auctionEnd;
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

    public Date getAuctionStart() {
        return auctionStart;
    }

    public void setAuctionStart(Date auctionStart) {
        this.auctionStart = auctionStart;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public static void main(String[] args) {
        QueryProcessor.processWithCustomSchema(entityManager -> {
            final int id = new Random().nextInt(10_000);

            final Item item = new Item("item-" + id, new Date(), new Date());
            entityManager.persist(item);

            final User user = new User("nickname-" + id, id + "@mail.com");
            entityManager.persist(user);

            final Bid bid = new Bid(new BigDecimal("1.0"), item);
            entityManager.persist(bid);
        });
    }
}


