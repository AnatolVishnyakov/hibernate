package com.orm.hibernate.jta.concurrency;

import com.orm.hibernate.jta.model.Item;
import com.orm.hibernate.jta.utils.QueryProcessor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.UUID;

public class NonTransactionalExample {
    public static void main(String[] args) {
        final Item createdItem = QueryProcessor.process(entityManager -> {
            final Item itm = new Item(UUID.randomUUID().toString());
            entityManager.persist(itm);
            return itm;
        });

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HibernateEx");

        EntityManager em = emf.createEntityManager(); // 1
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        final Item item = em.find(Item.class, createdItem.getId()); // 2
        item.setName("New Name");

        final String originalName = em.createQuery("select i.name from Item i where i.id = :id", String.class)
                .setParameter("id", createdItem.getId())
                .getSingleResult(); // 3
        System.out.println("1: " + originalName);

        final String newName = em.createQuery("select i from Item i where i.id = :id", Item.class)
                .setParameter("id", createdItem.getId())
                .getSingleResult()
                .getName(); // 4
        System.out.println("2: " + newName);

//        em.flush(); // 5
        em.refresh(item); // 6

        tx.commit();
        em.close();

    }
}
