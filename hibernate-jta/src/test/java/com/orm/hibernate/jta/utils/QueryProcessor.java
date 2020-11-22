package com.orm.hibernate.jta.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

public class QueryProcessor {
    public static void process(Consumer<EntityManager> consumer) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        consumer.accept(em);

        tx.commit();
        em.close();
    }

    public static <R> R process(Function<EntityManager, R> function) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final R result = function.apply(em);

        tx.commit();
        em.close();
        return result;
    }
}