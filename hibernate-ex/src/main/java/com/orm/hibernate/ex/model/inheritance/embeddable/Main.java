package com.orm.hibernate.ex.model.inheritance.embeddable;

import com.orm.hibernate.ex.model.EntitySaver;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
//            final Item item = new Item();
//            item.setDimensions(new Dimensions(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3)));
//            item.setWeight(new Weight(new BigDecimal(4)));
//            entityManager.persist(item);
            final List<Item> result = entityManager.createQuery("select i from Item i", Item.class).getResultList();
            result.forEach(item -> {
                final Measurement dimensions = item.getDimensions();
                final Measurement weight = item.getWeight();
                System.out.println("Dimensions: " + dimensions);
                System.out.println("Weight: " + weight);
            });
        });
    }
}
