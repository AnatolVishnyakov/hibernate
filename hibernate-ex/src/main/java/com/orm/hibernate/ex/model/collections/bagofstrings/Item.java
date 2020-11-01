package com.orm.hibernate.ex.model.collections.bagofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @Column(name = "FILENAME")
    @GenericGenerator(name = "ID_GENERATOR", strategy = "increment")
    @CollectionId(
            columns = {@Column(name = "IMAGE_ID")},
            type = @Type(type = "long"),
            generator = "ID_GENERATOR"
    )
    private Collection<String> images = new ArrayList<>();
    @Column(name = "NAME")
    private String name;

    public Item() {
    }

    public Collection<String> getImages() {
        return images;
    }

    public void setImages(Collection<String> images) {
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
