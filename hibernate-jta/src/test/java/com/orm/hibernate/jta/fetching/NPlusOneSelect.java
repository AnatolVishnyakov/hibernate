package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.Bid;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.Item;
import com.orm.hibernate.jta.model.fetching.nplusoneselects.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NPlusOneSelect extends JPATest {
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
        for (int i = 0; i < 2; i++) {
            final User seller = new User("JohnDoe");
            em.persist(seller);

            createdItem = new Item("Original Name", new Date(), seller);
            em.persist(createdItem);

            for (int j = 0; j < 3; j++) {
                final User bidder = new User(UUID.randomUUID().toString());
                em.persist(bidder);
                final Bid bid = new Bid(createdItem, bidder, BigDecimal.valueOf(new Random().nextDouble()));
                em.persist(bid);
            }
        }

        tx.commit();
        em.close();
    }

    @Test
    void nPlusOneSelectSeller() {
        final EntityManager em = JPA.createEntityManager();
        final List<Item> items = em.createQuery("select i from Item i", Item.class).getResultList();

        for (Item item : items) {
            Assertions.assertNotNull(item.getSeller().getUsername());
        }
    }

    @Test
    void nPlusOneSelectBid() {
        final EntityManager em = JPA.createEntityManager();
        final List<Item> items = em.createQuery("select i from Item i", Item.class).getResultList();

        for (Item item : items) {
            Assertions.assertTrue(item.getBids().size() > 0);
        }
    }
}
