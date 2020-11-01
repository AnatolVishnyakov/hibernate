package com.orm.hibernate.ex.model.collections.ordered.bagofstringsorderby;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
    @Column(name = "FILENAME")
    @GenericGenerator(name = "ID_GENERATOR", strategy = "increment")
    @CollectionId(
            columns = @Column(name = "IMAGE_ID"),
            type = @Type(type = "long"),
            generator = "ID_GENERATOR"
    )
    @org.hibernate.annotations.OrderBy(clause = "FILENAME desc")
    private Collection<String> images = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getImages() {
        return images;
    }

    public void setImages(Collection<String> images) {
        this.images = images;
    }

    public static void main(String[] args) {
        final Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final List<String> images = (List<String>) item.getImages();
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