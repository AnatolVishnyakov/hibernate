package com.orm.hibernate.ex.model.collections.sorted.sortedsetofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @Column(name = "IMAGENAME")
    @SortNatural
    private SortedSet<String> images = new TreeSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedSet<String> getImages() {
        return images;
    }

    public void setImages(SortedSet<String> images) {
        this.images = images;
    }

    public static void main(String[] args) {
        Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final SortedSet<String> images = item.getImages();
            images.add("test7");
            images.add("test9");
            images.add("test3");
            images.add("test2");
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach(System.out::println);
        });
    }
}
