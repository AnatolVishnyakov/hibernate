package com.orm.hibernate.ex.model.complexschemas.custom;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ITEM_ID")
    )
    protected Item item;

    @NotNull
    protected BigDecimal amount;

    @NotNull
    protected Date createdOn = new Date();

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
