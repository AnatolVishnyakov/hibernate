package com.orm.hibernate.ex.model.collections.embeddable.bagofembeddables;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
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
    @GenericGenerator(name = "ID_GENERATOR", strategy = "increment")
    @CollectionId(
            columns = @Column(name = "IMAGE_ID"),
            type = @Type(type = "long"),
            generator = "ID_GENERATOR"
    )
    private Collection<Image> images = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Image> getImages() {
        return images;
    }

    public void setImages(Collection<Image> images) {
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
            final Collection<Image> images = item.getImages();
            images.add(new Image(null, "fileName-test-91", 30, 40));
            images.add(new Image("test-51", "fileName-test-51", 30, 40));
            images.add(new Image(null, "fileName-test-3", 30, 40));
            images.add(new Image("test-49", "fileName-test-49", 30, 40));
            images.add(new Image("test-49", "fileName-test-49", 30, 40));
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach(System.out::println);
        });
    }
}