package com.orm.hibernate.jta.querying;

import com.orm.hibernate.jta.model.querying.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateExecuteQueries extends QueryingTest {

    @BeforeEach
    public void init() {
        System.getProperty("keepSchema", "false");
    }

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

    @Test
    public void createTypedQueries() throws Exception {
        TestDataCategoriesItems testData = storeTestData();
        Long ITEM_ID = testData.items.getFirstId();

        EntityManager em = JPA.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        {
            Query query = em.createQuery(
                    "select i from Item i where i.id = :id"
            ).setParameter("id", 5L);

            Item result = (Item) query.getSingleResult();

            assertEquals(result.getId(), ITEM_ID);
        }

        {
            TypedQuery<Item> query = em.createQuery(
                    "select i from Item i where i.id = :id", Item.class
            ).setParameter("id", ITEM_ID);

            // не нужно кастовать
            Item result = query.getSingleResult();

            assertEquals(result.getId(), ITEM_ID);
        }

        {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
            Root<Item> i = criteria.from(Item.class);
            criteria.select(i).where(cb.equal(i.get("id"), ITEM_ID));

            TypedQuery<Item> query = em.createQuery(criteria);

            // не нужно кастовать
            Item result = query.getSingleResult();

            assertEquals(result.getId(), ITEM_ID);
        }

        tx.commit();
        em.close();
    }
}