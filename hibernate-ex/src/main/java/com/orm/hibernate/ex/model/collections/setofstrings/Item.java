package com.orm.hibernate.ex.model.collections.setofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ElementCollection
    @CollectionTable(name = "IMAGE", joinColumns = @JoinColumn(name = "ITEM_ID"))
    @Column(name = "FILENAME")
    private Set<String> images = new HashSet<>();
    @Column(name = "NAME")
    private String name;

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
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
                ", images=" + images +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            item.getImages().add("File-1");
            item.setName("file test");
            entityManager.persist(item);
        });
    }
}
