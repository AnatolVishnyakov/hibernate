package com.orm.model.generator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ItemMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CaveatEmptor");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        ItemOne itemOne = new ItemOne();
        em.persist(itemOne);

        ItemTwo itemTwo = new ItemTwo();
        em.persist(itemTwo);

        tx.commit();
        em.close();
    }
}
