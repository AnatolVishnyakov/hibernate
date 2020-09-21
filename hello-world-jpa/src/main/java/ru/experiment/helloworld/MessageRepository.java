package ru.experiment.helloworld;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class MessageRepository {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HelloWorldPU");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        Message message = new Message();
        message.setText("Hello World!");
        em.persist(message);
        message.setText("Replace");

        tx.commit();
        em.close();
    }
}
