package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.subselect.Bid;
import com.orm.hibernate.jta.model.fetching.subselect.Item;
import com.orm.hibernate.jta.model.fetching.subselect.User;
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

public class SubSelect extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingSubselectPU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        for (int i = 0; i < 100; i++) {
            final User seller = new User("JohnDoe + " + UUID.randomUUID().toString());
            em.persist(seller);

            createdItem = new Item("Original Name", new Date(), seller);
            em.persist(createdItem);

            for (int j = 0; j < 5; j++) {
                final Bid bid = new Bid(createdItem, seller, BigDecimal.valueOf(new Random().nextDouble()));
                em.persist(bid);
            }
        }

        tx.commit();
        em.close();
    }

    @Test
    void subSelectCollection() {
        final EntityManager em = JPA.createEntityManager();
        final List<Item> items = em.createQuery("select i from Item i", Item.class).getResultList();

        for (Item item : items) {
            // Hibernate запомнит запрос items,
            // затем встроит этот запрос внутрь
            // подзапроса bids
            final int bidSize = item.getBids().size();
            Assertions.assertTrue(bidSize > 0);
        }
    }
}
