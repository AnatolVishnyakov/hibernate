package com.orm.hibernate.ex.model.collections.mapofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.*;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @MapKeyColumn(name = "FILENAME")
    @Column(name = "IMAGENAME")
    private Map<String, String> images = new HashMap<>();

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static Item newItem(String name, Map<String, String> images) {
        final Item item = new Item();
        item.setName(name);
        item.setImages(images);
        return item;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            entityManager.persist(newItem("Foo", new HashMap<String, String>(){{
                put("foo.jpg", "Foo");
                put("bar.jpg", "Bar");
                put("baz.jpg", "Baz");
            }}));
            entityManager.persist(newItem("A", Collections.singletonMap("a1.jpg", "A1")));
            entityManager.persist(newItem("B", new HashMap<String, String>(){{
                put("b1.jpg", "B1");
                put("b2.jpg", "B2");
            }}));
            entityManager.persist(newItem("C", Collections.emptyMap()));
        });
    }
}
