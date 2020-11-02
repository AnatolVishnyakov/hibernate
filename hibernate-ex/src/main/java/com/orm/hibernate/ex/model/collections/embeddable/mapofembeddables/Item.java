package com.orm.hibernate.ex.model.collections.embeddable.mapofembeddables;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.collections.embeddable.mapofstringsembeddables.Image;

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
    private Map<Filename, Image> images = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Filename, Image> getImages() {
        return images;
    }

    public void setImages(Map<Filename, Image> images) {
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
            final Map<Filename, Image> images = item.getImages();
            images.put(new Filename("fileName-test-91", "jpg"), new Image(null, 30, 40));
            images.put(new Filename("fileName-test-51", "jpg"), new Image("test-51", 30, 40));
            images.put(new Filename("fileName-test-3", "jpg"), new Image(null, 30, 40));
            images.put(new Filename("fileName-test-49", "jpg"), new Image("test-49", 30, 40));
            images.put(new Filename("fileName-test-49", "jpg"), new Image("test-49", 35, 40));
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