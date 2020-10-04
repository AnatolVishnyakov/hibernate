package com.orm.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

@Entity
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Item {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;
    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    private String name;
    private String description;
    private Date createdOn;
    private boolean verified;
    private AuctionType auctionType;
    private BigDecimal initialPrice;
    private Date auctionStart;
    @Future
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
}
