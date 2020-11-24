package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.interception.Item;
import com.orm.hibernate.jta.model.fetching.interception.User;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LazyInterception extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingInterceptionPU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        final User seller = new User("JohnDoe");
        em.persist(seller);
        createdItem = new Item("Original Name", new Date(), seller, "Original Description");
        em.persist(createdItem);

        tx.commit();
        em.close();
    }

    @Test
    void withoutProxyClass() {
        final EntityManager em = JPA.createEntityManager();
        // Будет выполнен select запрос
        final User user = em.getReference(User.class, createdItem.getSeller().getId());
        assertTrue(Hibernate.isInitialized(user));
    }

    @Test
    void loadSellerWhenCall() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        assertEquals(item.getSeller().getId(), createdItem.getSeller().getId());
    }

    @Test
    void loadPrimitiveWhenCall() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        assertTrue(item.getDescription().length() > 0);
    }
}
