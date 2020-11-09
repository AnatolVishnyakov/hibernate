package com.orm.hibernate.ex.model.associations.maps.mapkey;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    protected Item item;
    @NotNull
    protected BigDecimal amount;

    public Bid() {
    }

    public Bid(BigDecimal amount, Item item) {
        this.amount = amount;
        this.item = item;
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", item=" + item +
                ", amount=" + amount +
                '}';
    }
}