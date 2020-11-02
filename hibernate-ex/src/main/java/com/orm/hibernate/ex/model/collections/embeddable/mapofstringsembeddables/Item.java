package com.orm.hibernate.ex.model.collections.embeddable.mapofstringsembeddables;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private Map<String, Image> images = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Image> getImages() {
        return images;
    }

    public void setImages(Map<String, Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        final Item item = new Item();
        item.setName("Foo-" + new Random().nextInt(10_000));
        QueryProcessor.process(entityManager -> {
            final Map<String, Image> images = item.getImages();
            images.put("fileName-test-91", new Image(null, 30, 40));
            images.put("fileName-test-51", new Image("test-51", 30, 40));
            images.put("fileName-test-3", new Image(null, 30, 40));
            images.put("fileName-test-49", new Image("test-49", 30, 40));
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().keySet().forEach(key -> {
                System.out.println(key + " : " + resItem.getImages().get(key));
            });
        });
    }
}