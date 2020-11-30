package com.orm.hibernate.jta.model.fetching.eagerselect;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String name;
    @NotNull
    protected Date auctionEnd;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SELECT // По умолчанию JOIN
    )
    protected User seller;
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SELECT // По умолчанию JOIN
    )
    protected Set<Bid> bids = new HashSet<>();

    public Item() {
    }

    public Item(String name, Date auctionEnd, User seller) {
        this.name = name;
        this.auctionEnd = auctionEnd;
        this.seller = seller;
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

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }
}
