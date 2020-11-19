package com.orm.hibernate.jta.concurrency;

import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OptimisticLockExample {
    public static void main(String[] args) {
        final Item item = new Item(UUID.randomUUID().toString());
        QueryProcessor.process(entityManager -> {
            entityManager.persist(item);
        });

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            QueryProcessor.process(entityManager -> {
                final Item item1 = entityManager.find(Item.class, item.getId());
                System.out.println(item1.getVersion());
                item1.setName("thread-1");
            });
        });

        executorService.execute(() -> {
            QueryProcessor.process(entityManager -> {
                final Item item1 = entityManager.find(Item.class, item.getId());
                System.out.println(item1.getVersion());
                item1.setName("thread-2");
            });
        });
        executorService.shutdown();
    }
}
