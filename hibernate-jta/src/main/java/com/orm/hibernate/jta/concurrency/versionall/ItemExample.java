package com.orm.hibernate.jta.concurrency.versionall;

import com.orm.hibernate.jta.utils.QueryProcessor;

import java.util.UUID;

public class ItemExample {
    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            item.setName(UUID.randomUUID().toString());
            entityManager.persist(item);
        });
    }
}
