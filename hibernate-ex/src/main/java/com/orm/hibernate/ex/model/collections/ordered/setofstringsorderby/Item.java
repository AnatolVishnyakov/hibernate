package com.orm.hibernate.ex.model.collections.ordered.setofstringsorderby;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @Column(name = "FILENAME")
//    @javax.persistence.OrderBy(value = "desc")
    @org.hibernate.annotations.OrderBy(clause = "FILENAME desc")
//    @org.hibernate.annotations.OrderBy(clause = "substring(FILENAME, 4, 8) desc")
    private Set<String> images = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public static void main(String[] args) {
        final Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final Set<String> images = item.getImages();
            images.add("test-51");
            images.add("test-91");
            images.add("test-3");
            images.add("test-49");
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach(System.out::println);
        });
    }
}