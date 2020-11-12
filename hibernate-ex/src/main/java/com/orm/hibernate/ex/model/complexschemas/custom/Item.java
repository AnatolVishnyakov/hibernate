package com.orm.hibernate.ex.model.complexschemas.custom;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
@Check(constraints = "AUCTIONSTART < AUCTIONEND")
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
        QueryProcessor.process(entityManager -> {
            final int id = new Random().nextInt(10_000);

            final Date auctionStart = new Date();
            final java.sql.Date auctionEnd = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
            final Item item = new Item("item-" + id, auctionStart, auctionEnd); // Сработает проверка даты @check
            entityManager.persist(item);

            final User user = new User("nickname-" + id, id + "@mail.com");
            entityManager.persist(user);

            final Bid bid = new Bid(new BigDecimal("1.0"), item);
            entityManager.persist(bid);
        });

//        QueryProcessor.process(entityManager -> {
//            final User user = new User("nickname-test", "nickname@mail.com");
//            entityManager.persist(user);
//        });
    }
}


