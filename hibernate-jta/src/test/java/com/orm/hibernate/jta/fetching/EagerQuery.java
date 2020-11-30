package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.Bid;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.Item;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EagerQuery extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingNPlusOneSelectsPU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        final User seller = new User("JohnDoe");
        em.persist(seller);

        createdItem = new Item("Original Name", new Date(), seller);
        em.persist(createdItem);

        for (int i = 0; i < 3; i++) {
            final Bid bid = new Bid(createdItem, seller, BigDecimal.valueOf(new Random().nextDouble()));
            em.persist(bid);
        }

        tx.commit();
        em.close();
    }

    @Test
    void eagerQuery() {
        final EntityManager em = JPA.createEntityManager();
        final List<Item> items = em.createQuery("select i from Item i join fetch i.seller", Item.class)
                .getResultList();

        em.close();

        for (Item item : items) {
            assertNotNull(item.getSeller().getUsername());
        }
    }

    @Test
    void eagerCriteriaQuery() {
        final EntityManager em = JPA.createEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        final Root<Item> i = criteria.from(Item.class);
        i.fetch("seller");
        criteria.select(i);

        final List<Item> items = em.createQuery(criteria).getResultList();
        em.close();

        for (Item item : items) {
            assertNotNull(item.getSeller().getUsername());
        }
    }

    @Test
    void eagerItemsWithAllBids() {
        final EntityManager em = JPA.createEntityManager();
        final List<Item> items = em.createQuery("select i from Item i left join fetch i.bids", Item.class)
                .getResultList();
        em.close();

        for (Item item : items) {
            assertTrue(item.getBids().size() > 0);
        }
    }

    @Test
    void eagerItemsWithAllBidsCriteriaQuery() {
        final EntityManager em = JPA.createEntityManager();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        final Root<Item> i = criteria.from(Item.class);
        i.fetch("bids", JoinType.LEFT);
        criteria.select(i);

        final List<Item> items = em.createQuery(criteria).getResultList();
        em.close();

        for (Item item : items) {
            assertTrue(item.getBids().size() > 0);
        }
    }
}