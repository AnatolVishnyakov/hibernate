package com.orm.hibernate.jta.model.fetching.fetchloadgraph;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph, // По умолчанию "Item" entity graph
        @NamedEntityGraph(
                name = "ItemSeller",
                attributeNodes = {
                        @NamedAttributeNode("seller")
                }
        )
})
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
    @ManyToOne(fetch = FetchType.LAZY)
    protected User seller;
    @OneToMany(mappedBy = "item")
    protected Set<Bid> bids = new HashSet<>();
    @ElementCollection
    protected Set<String> images = new HashSet<>();

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

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }
}
