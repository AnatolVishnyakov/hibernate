package com.orm.hibernate.ex.model.associations.maps.ternary;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    private String name;

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

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        final int num = new Random().nextInt(10_000);
        QueryProcessor.process(entityManager -> {
//            final Category category = new Category("category-" + num);
//            entityManager.persist(category);
//
//            final Item someItem = new Item("item-" + num);
//            entityManager.persist(someItem);
//            final Item otherItem = new Item("other" + num);
//            entityManager.persist(otherItem);
//
//            category.getItemAddedBy().put(someItem, new User("user-" + num));
//            category.getItemAddedBy().put(otherItem, new User("user-" + num));

            final Category category = entityManager.find(Category.class, 497L);
            final Item someItem = category.getItemAddedBy().keySet().iterator().next();
            System.out.println("Some Item: " + someItem.getId());
            System.out.println("Some User: " + category.getItemAddedBy().get(someItem));

            category.getItemAddedBy().remove(someItem);
            System.out.println(category);
        });
    }
}
