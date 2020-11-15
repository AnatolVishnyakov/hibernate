package com.orm.hibernate.ex.model.entitymanager;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.entitymanager.model.Item;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

import static java.lang.String.format;

public class SimpleTransaction {
    // Сущность находится во временном состоянии
    private static <T> boolean isDraft(EntityManager em, T entity) {
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity) == null;
    }

    // Сущность находится в хранимом состоянии
    private static <T> boolean isPersist(EntityManager em, T entity) {
        return em.contains(entity);
    }

    // Сущность находится в отсоединенном состоянии
    private static <T> boolean isDetach(EntityManager em, T entity) {
        return !isPersist(em, entity) &&
                !isDraft(em, entity);
    }

    private static <T> void printStateEntity(EntityManager em, T entity) {
        final String message = format("Persist: %s | Detach: %s | Draft: %s",
                isPersist(em, entity),
                isDetach(em, entity),
                isDraft(em, entity)
        );

        System.out.println(message);
    }

    public static <T> void newEntity(Class<T> clazz) {
        final int number = new Random().nextInt(100_000);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            final T entity = clazz.newInstance();
            if (clazz.isInstance(Item.class)) {
                ((Item) entity).setName("item-" + number);
            }

            printStateEntity(em, entity);
            em.persist(entity);
            printStateEntity(em, entity);

            em.detach(entity);
            printStateEntity(em, entity);

            // Синхронизирует/извлекает persistence context
            tx.commit();
        } catch (Exception exc) {
            // Откат транзакции
            tx.rollback();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public static void main(String[] args) {
//        QueryProcessor.process(entityManager -> {
//            final Item item = entityManager.find(Item.class, 70L);
//            if (item != null) {
//                item.setName("New Name " + new Random().nextInt());
//            }
//            item.setAuctionEnd(new Date());
//        });

        QueryProcessor.process(entityManager -> {
            final Item itemA = entityManager.find(Item.class, 71L);
            final Item itemB = entityManager.find(Item.class, 71L);
            System.out.println(itemA == itemB);
            System.out.println(itemA.equals(itemB));
            System.out.println(itemA.getId().equals(itemB.getId()));
        });
    }
}
