package com.orm.hibernate.ex.model.associations.onetomany.cascade.ondelete;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Bid> bids = new HashSet<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Item item = new Item("test-" + new Random().nextInt(10_000));
        QueryProcessor.process(entityManager -> {
            entityManager.persist(item);
        });

        // Зависимые элементы сохраняются
        // автоматически
        QueryProcessor.process(entityManager -> {
            final Item itemUpdate = entityManager.createQuery("from Item i where i.id = " + item.id, Item.class)
                    .getSingleResult();
            itemUpdate.getBids().addAll(Arrays.asList(
                    new Bid(1.0f, item),
                    new Bid(3.0f, item),
                    new Bid(5.0f, item),
                    new Bid(6.0f, item)
            ));

            entityManager.persist(itemUpdate);
        });

        QueryProcessor.process(entityManager -> {
            final Item it = entityManager.find(Item.class, item.id);
            entityManager.remove(it);
        });
    }
}