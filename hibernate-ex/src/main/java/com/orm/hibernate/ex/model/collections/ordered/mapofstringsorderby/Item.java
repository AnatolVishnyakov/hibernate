package com.orm.hibernate.ex.model.collections.ordered.mapofstringsorderby;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @org.hibernate.annotations.OrderBy(clause = "FILENAME desc")
    private Map<String, String> images = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public static void main(String[] args) {
        final Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final Map<String, String> images = item.getImages();
            images.put("test-51", "value-62");
            images.put("test-91", "value-34");
            images.put("test-3", "value-78");
            images.put("test-49", "value-99");
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach((s, s2) -> System.out.println(s + " " + s2));
        });
    }
}