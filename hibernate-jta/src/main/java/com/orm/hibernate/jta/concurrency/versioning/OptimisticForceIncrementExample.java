package com.orm.hibernate.jta.concurrency.versioning;

import com.orm.hibernate.jta.model.Bid;
import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class OptimisticForceIncrementExample {
    private static Item createItem() {
        return QueryProcessor.process(entityManager -> {
//            final Item item = new Item(UUID.randomUUID().toString());
//            entityManager.persist(item);
//
//            for (int i = 0; i < 10; i++) {
//                final Bid bid = new Bid(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10_000)), item);
//                entityManager.persist(bid);
//            }
//            return item;

            return entityManager.find(Item.class, 300L);
        });
    }

    private static Bid queryHighestBid(EntityManager em, Item item) {
        // Can't scroll with cursors in JPA, have to use setMaxResult()
        try {
            return (Bid) em.createQuery(
                    "select b from Bid b" +
                            " where b.item = :itm" +
                            " order by b.amount desc"
            )
                    .setParameter("itm", item)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        final Item createdItem = createItem();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final Item item = em.find(Item.class, createdItem.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        final Bid highestBid = queryHighestBid(em, item);
        System.out.println("Max: " + highestBid.getAmount());

        final Bid newBid = new Bid(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(30_000)), item);
        System.out.println("New bid: " + newBid.getAmount());
        if (newBid.getAmount().doubleValue() > highestBid.getAmount().doubleValue()) {
            em.persist(newBid);
        }

        tx.commit();
        em.close();
    }
}
