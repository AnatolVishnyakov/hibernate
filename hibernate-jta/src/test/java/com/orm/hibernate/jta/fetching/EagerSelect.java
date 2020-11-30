package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.eagerselect.Bid;
import com.orm.hibernate.jta.model.fetching.eagerselect.Item;
import com.orm.hibernate.jta.model.fetching.eagerselect.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EagerSelect extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingEagerSelectPU");
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
    void eagerSelect() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        em.detach(item);

        assertEquals(item.getBids().size(), 3);
        assertNotNull(item.getBids().iterator().next().getAmount());
        assertEquals(item.getSeller().getUsername(), "JohnDoe");
    }
}
