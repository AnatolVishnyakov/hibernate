package com.orm.hibernate.jta.concurrency.locking;

import com.orm.hibernate.jta.model.Category;
import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PessimisticLockExample {
    private static Set<Category> createCategories() {
        return QueryProcessor.process(entityManager -> {
            Set<Category> categories = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                final Category category = new Category(UUID.randomUUID().toString());
                entityManager.persist(category);
                categories.add(category);

                final Item item = new Item(UUID.randomUUID().toString());
                item.setCategory(category);
                item.setBuyNowPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()));
                entityManager.persist(item);
            }
            return categories;
        });
    }

    public static void main(String[] args) {
        final Set<Long> categories =
                new HashSet<>(Arrays.asList(302L));
//                createCategories()
//                .stream().map(Category::getId)
//                .collect(Collectors.toSet());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        BigDecimal totalPrice = new BigDecimal(0);
        for (Long categoryId : categories) {
            final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class)
//                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    // Время ожидания в случае взятия
                    // пессимистичного лока
                    .setHint("javax.persistence.lock.timeout", 5_000)
                    .setParameter("categoryId", categoryId)
                    .getResultList();

            for (Item item : items) {
                totalPrice = totalPrice.add(item.getBuyNowPrice());
            }
        }

        tx.commit();
        em.close();
    }
}
