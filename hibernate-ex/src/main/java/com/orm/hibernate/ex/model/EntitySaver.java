package com.orm.hibernate.ex.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class EntitySaver {
    public static void save(Consumer<EntityManager> consumer) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        consumer.accept(em);

        tx.commit();
        em.close();
    }
}
