package com.orm.hibernate.ex.model.associations.manytomany.ternary;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Random;
import java.util.Set;

@Embeddable
public class CategorizedItem {
    @ManyToOne
    @JoinColumn(
            name = "ITEM_ID",
            nullable = false,
            updatable = false
    )
    private Item item;
    @ManyToOne
    @JoinColumn(
            name = "USER_ID",
            updatable = false
    )
    @NotNull
    private User addedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @NotNull
    private Date addedOn = new Date();

    public CategorizedItem() {
    }

    public CategorizedItem(Item item, User addedBy) {
        this.item = item;
        this.addedBy = addedBy;
    }

    public Item getItem() {
        return item;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public static void main(String[] args) {
        final int id = new Random().nextInt(10_000);

        final Item someItem = new Item("Some item #" + id);
        QueryProcessor.process(entityManager -> {
            final Category someCategory = new Category("Some category #" + id);
            final Category otherCategory = new Category("Other category #" + id);
            entityManager.persist(someCategory);
            entityManager.persist(otherCategory);

            final Item otherItem = new Item("Other item #" + id);
            entityManager.persist(someItem);
            entityManager.persist(otherItem);

            final User user = new User("John Doe #" + id);
            entityManager.persist(user);

            final CategorizedItem linkOne = new CategorizedItem(someItem, user);
            someCategory.getCategorizedItems().add(linkOne);

            final CategorizedItem linkTwo = new CategorizedItem(otherItem, user);
            otherCategory.getCategorizedItems().add(linkTwo);
        });

        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, someItem.getId());
            final Category category = (Category) entityManager.createQuery(
                    "select c from Category c " +
                            "join c.categorizedItems ci " +
                            "where ci.item = :item")
                    .setParameter("item", item)
                    .getSingleResult();
            System.out.println(category);
            final Set<CategorizedItem> categorizedItems = category.getCategorizedItems();
            final CategorizedItem categorizedItem = categorizedItems.iterator().next();
            System.out.println(categorizedItem.getItem());
            System.out.println(categorizedItem.getAddedBy());
            System.out.println(categorizedItem.getAddedOn());
        });
    }
}
