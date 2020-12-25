package com.orm.hibernate.jta.querying;

import com.orm.hibernate.jta.model.querying.Item;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateExecuteQueries extends QueryingTest {

    @Test
    public void createQueries() throws Exception {
        storeTestData();

        EntityManager em = JPA.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        {
            Query query = em.createQuery("select i from Item i");
            assertEquals(query.getResultList().size(), 3);
        }

        {
            // Удобно использовать, когда нужен
            // результат зависимый от тумблеров/полей
            // заполненных пользователем
            CriteriaBuilder cb = em.getCriteriaBuilder();
            // Доступен из EntityManagerFactory
            // CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

            CriteriaQuery criteria = cb.createQuery();
            criteria.select(criteria.from(Item.class));

            Query query = em.createQuery(criteria);

            assertEquals(query.getResultList().size(), 3);
        }

        {
            // Устаревший метод JPA 1.0 который
            // не возвращает TypedQuery!
            // Полезен когда используются
            // функциональности конкретной БД
            Query query = em.createNativeQuery(
                    "select * from ITEM", Item.class
            );

            assertEquals(query.getResultList().size(), 3);
        }

        tx.commit();
        em.close();
    }
}