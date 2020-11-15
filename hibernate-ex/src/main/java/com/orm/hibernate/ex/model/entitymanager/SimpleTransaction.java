package com.orm.hibernate.ex.model.entitymanager;

import com.orm.hibernate.ex.model.entitymanager.model.Item;

import javax.persistence.*;
import java.util.Random;

import static java.lang.String.format;

public class SimpleTransaction {
    // Сущность находится во временном состоянии
    private static boolean isDraft(EntityManager em, Item item) {
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(item) == null;
    }

    // Сущность находится в хранимом состоянии
    private static boolean isPersist(EntityManager em, Item item) {
        return em.contains(item);
    }

    // Сущность находится в отсоединенном состоянии
    private static boolean isDetach(EntityManager em, Item item) {
        return !isPersist(em, item) &&
                !isDraft(em, item);
    }

    private static void stateEntity(EntityManager em, Item item) {
        final String message = format("Persist: %s | Detach: %s | Draft: %s",
                isPersist(em, item),
                isDetach(em, item),
                isDraft(em, item)
        );

        System.out.println(message);
    }

    public static void main(String[] args) {
        final int number = new Random().nextInt(100_000);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            final Item item = new Item();
            item.setName("item-" + number);
            stateEntity(em, item);
            em.persist(item);
            stateEntity(em, item);

            em.detach(item);
            stateEntity(em, item);

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
}
