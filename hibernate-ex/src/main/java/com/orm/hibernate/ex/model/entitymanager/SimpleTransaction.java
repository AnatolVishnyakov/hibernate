package com.orm.hibernate.ex.model.entitymanager;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.entitymanager.model.Item;
import org.hibernate.Hibernate;

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

    // Загружен объект из БД в контекст хранения
    private static <T> boolean isLoaded(EntityManager em, T entity) {
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(entity);
    }

    private static <T> void printStateEntity(EntityManager em, T entity) {
        final String message = format(
                "\n\t Persist: \t%s \t|" +
                "\n\t Detach: \t%s \t|" +
                "\n\t Draft: \t%s \t|" +
                "\n\t Loaded: \t%s \t|" +
                "\n",
                isPersist(em, entity),
                isDetach(em, entity),
                isDraft(em, entity),
                isLoaded(em, entity)
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

    public static void exampleEqualsReference() {
        QueryProcessor.process(entityManager -> {
            final Item itemA = entityManager.find(Item.class, 71L);
            final Item itemB = entityManager.find(Item.class, 71L);
            System.out.println(itemA == itemB);
            System.out.println(itemA.equals(itemB));
            System.out.println(itemA.getId().equals(itemB.getId()));
        });
    }

    public static void exampleGetAndUpdateItem() {
        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.find(Item.class, 70L);
            if (item != null) {
                item.setName("New Name " + new Random().nextInt());
                item.setAuctionEnd(new Date());
            }
        });
    }

    // Если не нужно обращаться к БД
    // используется getReference
    public static void exampleGetReferenceItem() {
        QueryProcessor.process(entityManager -> {
            final Item item = entityManager.getReference(Item.class, 72L);
            System.out.println(item.getId());

            printStateEntity(entityManager, item);
            // Загрузка данных прокси-объекта
            Hibernate.initialize(item);
            printStateEntity(entityManager, item);
        });
    }

    public static void main(String[] args) {
        exampleGetReferenceItem();
    }
}
