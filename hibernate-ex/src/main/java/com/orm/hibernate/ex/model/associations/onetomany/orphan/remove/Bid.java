package com.orm.hibernate.ex.model.associations.onetomany.orphan.remove;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    protected Item item;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    protected User bidder;
    @NotNull
    protected BigDecimal amount;

    public Bid() {
    }

    public Bid(BigDecimal amount, Item item) {
        this.amount = amount;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
