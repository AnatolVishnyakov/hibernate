package com.orm.hibernate.ex.model.associations.onetomany.orphan;

import javax.persistence.*;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private Float amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    public Bid() {
    }

    public Bid(Float amount, Item item) {
        this.amount = amount;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", item=" + item +
                '}';
    }
}
