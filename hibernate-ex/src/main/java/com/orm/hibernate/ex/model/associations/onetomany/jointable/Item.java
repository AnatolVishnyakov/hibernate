package com.orm.hibernate.ex.model.associations.onetomany.jointable;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_BUYER",
            joinColumns = @JoinColumn(name = "ITEM_ID"),
            inverseJoinColumns = @JoinColumn(nullable = false)
    )
    private User buyer;

    public Item() {
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User user) {
        this.buyer = user;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            entityManager.persist(item);

            final User user = new User();
            entityManager.persist(user);

            final Item item1 = new Item();
            final User user1 = new User();
            item1.setBuyer(user1);
            entityManager.persist(user1);
            entityManager.persist(item1);
        });
    }
}
