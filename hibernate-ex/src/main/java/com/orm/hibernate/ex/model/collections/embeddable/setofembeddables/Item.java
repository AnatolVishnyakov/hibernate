package com.orm.hibernate.ex.model.collections.embeddable.setofembeddables;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private String name;
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    @AttributeOverride(
            name = "fileName",
            column = @Column(name = "FNAME", nullable = false)
    )
    private Set<Image> images = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
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
            final Set<Image> images = item.getImages();
            images.add(new Image("test-51", "fileName-test-51", 30, 40));
            images.add(new Image("test-91", "fileName-test-91", 30, 40));
            images.add(new Image("test-3", "fileName-test-3", 30, 40));
            images.add(new Image("test-49", "fileName-test-49", 30, 40));
            images.add(new Image("test-49", "fileName-test-49", 30, 40));
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach(image -> {
                System.out.println(image + ", parent: " + image.getItem());
            });
        });
    }
}