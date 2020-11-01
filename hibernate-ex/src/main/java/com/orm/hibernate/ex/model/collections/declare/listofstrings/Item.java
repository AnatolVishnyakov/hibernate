package com.orm.hibernate.ex.model.collections.declare.listofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @OrderColumn
    @Column(name = "FILENAME")
    private List<String> images = new ArrayList<>();
    @Column(name = "NAME")
    private String name;

    public Item() {
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static Long nextId() {
        Long count = (Long) QueryProcessor.process((Function<EntityManager, Object>) entityManager -> ((BigInteger) entityManager
                .createNativeQuery("select count(*) from image")
                .getSingleResult()).longValue()
        );
        return count++;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", images=" + images +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            Long counter = nextId();
            for (int i = 0; i < 10; i++) {
                item.getImages().add("File-" + counter++);
            }
            item.setName("file test " + new Random().nextInt());
            entityManager.persist(item);
        });
    }
}