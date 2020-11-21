package com.orm.hibernate.jta.concurrency.versioning;

import com.orm.hibernate.jta.model.Category;
import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class LockModeExample {
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
        final Set<Long> categories = createCategories()
                .stream().map(Category::getId)
                .collect(Collectors.toSet());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        BigDecimal totalPrice = new BigDecimal(0);
        for (Long categoryId : categories) {
            final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class)
                    // Hibernate осуществляет проверку версий
                    // при Optimistic, блокировки при этом
                    // не используются (на каждую строку отдельный
                    // проверяющий select запрос)
                    .setLockMode(LockModeType.OPTIMISTIC)
                    .setParameter("categoryId", categoryId)
                    .getResultList();

            for (Item item : items) {
                totalPrice = totalPrice.add(item.getBuyNowPrice());
            }
        }

        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.createQuery("SELECT i FROM Item i WHERE i.category.id = :categoryId", Item.class)
                    .setParameter("categoryId", categories.iterator().next())
                    .getSingleResult();
            item.setBuyNowPrice(new BigDecimal(100));
            entityManager.persist(item);
        });

        tx.commit();
        em.close();
    }
}
