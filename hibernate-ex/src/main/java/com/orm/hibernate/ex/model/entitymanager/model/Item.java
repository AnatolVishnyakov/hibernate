package com.orm.hibernate.ex.model.entitymanager.model;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Version
    protected long version;
    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    protected String name;
    @Future
    protected Date auctionEnd;
    @Transient
    protected Set<Bid> bids = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    protected Category category;
    protected BigDecimal buyNowPrice;

    public Item() {
        System.out.println("Создается Item");
    }

    public String getName() {
        return name;
    }

    public long getVersion() {
        return version;
    }

    public Long getId() {
        return id;
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

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addBid(Bid bid) {
        // Be defensive
        if (bid == null)
            throw new NullPointerException("Can't add null Bid");
        if (bid.getItem() != null)
            throw new IllegalStateException("Bid is already assigned to an Item");

        getBids().add(bid);
        bid.setItem(this);
    }

    public Bid placeBid(Bid currentHighestBid, BigDecimal bidAmount) {
        if (currentHighestBid == null ||
                bidAmount.compareTo(currentHighestBid.getAmount()) > 0) {
            return new Bid(bidAmount, this);
        }
        return null;
    }
}
