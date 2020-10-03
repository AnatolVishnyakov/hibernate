package com.orm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;
    private String name;
    private String description;
    private Date createdOn;
    private boolean verified;
    private AuctionType auctionType;
    private BigDecimal initialPrice;
    private Date auctionStart;
    private Date auctionEnd;
    private Set<Bid> bids = new HashSet<Bid>();

    public Long getId() {
        return id;
    }

    public Set<Bid> getBids() {
        return Collections.unmodifiableSet(bids);
    }

    void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public void addBid(Bid bid) {
        Objects.requireNonNull(bid, "Can't add null Bid");
        Objects.requireNonNull(bid.getItem(), "Bid is already assigned to an Item");
        // Объединяйте операции над ассоциациями
        getBids().add(bid);
        bid.setItem(this);
    }
}
