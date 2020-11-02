package com.orm.hibernate.ex.model.associations.onetomany;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Random;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private Float amount;
    @ManyToOne(fetch = FetchType.LAZY) // однонаправленная связь
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

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            Item item = new Item("test-" + new Random().nextInt(10_000));
            entityManager.persist(item);

            entityManager.persist(new Bid(1.0f, item));
            entityManager.persist(new Bid(3.0f, item));
            entityManager.persist(new Bid(5.0f, item));
            entityManager.persist(new Bid(6.0f, item));
        });
    }
}
