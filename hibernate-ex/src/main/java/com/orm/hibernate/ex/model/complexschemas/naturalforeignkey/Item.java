package com.orm.hibernate.ex.model.complexschemas.naturalforeignkey;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Random;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String name;
    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "SELLER_CUSTOMERNR",
            referencedColumnName = "CUSTOMERNR"
    )
    private User seller;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
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

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public static void main(String[] args) {
        String genId = String.valueOf(new Random().nextInt(10_000));
        QueryProcessor.process(entityManager -> {
            final User user = new User("test-" + genId);
            entityManager.persist(user);

            final Item item = new Item("item-" + genId);
            item.setSeller(user);
            entityManager.persist(item);
        });
    }
}
