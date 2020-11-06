package com.orm.hibernate.ex.model.associations.manytomany.bidirectional;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ManyToMany(mappedBy = "items")
    private Set<Category> categories = new HashSet<>();

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            Category category = new Category();
            category.getItems().add(item);
            item.getCategories().add(category);

            entityManager.persist(item);
            entityManager.persist(category);
        });
    }
}
