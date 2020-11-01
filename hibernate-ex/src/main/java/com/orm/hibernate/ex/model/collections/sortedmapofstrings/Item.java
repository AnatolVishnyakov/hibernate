package com.orm.hibernate.ex.model.collections.sortedmapofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.util.SortedMap;
import java.util.TreeMap;

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
    @SortComparator(ReverseStringComparator.class)
    private SortedMap<String, String> images = new TreeMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedMap<String, String> getImages() {
        return images;
    }

    public void setImages(SortedMap<String, String> images) {
        this.images = images;
    }

    public static void main(String[] args) {
        Item item = new Item();
        QueryProcessor.process(entityManager -> {
            final SortedMap<String, String> images = item.getImages();
            images.put("test" + 23, "test" + 41);
            images.put("test" + 14, "test" + 52);
            images.put("test" + 7, "test" + 92);
            images.put("test" + 9, "test" + 47);
            images.put("test" + 3, "test" + 57);
            entityManager.persist(item);
        });

        QueryProcessor.process(entityManager -> {
            Item resItem = (Item) entityManager.createNativeQuery("select * from item where id = " + item.id, Item.class)
                    .getSingleResult();
            resItem.getImages().forEach((s, s2) -> System.out.println(s + " " + s2));
        });
    }
}
