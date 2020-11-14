package com.orm.hibernate.ex.model.complexschemas.compositekey.manytoone;

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
    private String name;
    @NotNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "SELLER_USERNAME", referencedColumnName = "USERNAME"),
            @JoinColumn(name = "SELLER_DEPARTMENTNR", referencedColumnName = "DEPARTMENTNR")
    })
    private User seller;

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

    public Long getId() {
        return id;
    }

    public static void main(String[] args) {
        final Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final int genId = new Random().nextInt(10_000);
            final UserId userId = new UserId("John Doe #" + genId, String.valueOf(genId));
            final User user = new User(userId);
            entityManager.persist(user);

            item.setSeller(user);
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            final Item itm = entityManager.find(Item.class, item.getId());
            System.out.println(itm.getSeller());
        });
    }
}
