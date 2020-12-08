package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.cascade.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.Random;

public class Cascade extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringCascadePU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        final User seller = new User("JohnDoe");
        em.persist(seller);

        final BillingDetails bankAccount = new BankAccount("John Doe", "test-bank-acc", "Deutche", "Master Card");
        em.persist(bankAccount);
        seller.getBillingDetails().add(bankAccount);

        final BillingDetails creditCard = new CreditCard("John Doe", "2345234", "12", "25");
        em.persist(creditCard);
        seller.getBillingDetails().add(creditCard);

        createdItem = new Item("Original Name", seller);
        em.persist(createdItem);

        for (int i = 0; i < 2; i++) {
            final Bid bid = new Bid(BigDecimal.valueOf(new Random().nextDouble()), createdItem);
            em.persist(bid);
        }

        tx.commit();
        em.close();
    }

    @Test
    void cascadeDetach() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        Assertions.assertEquals(item.getBids().size(), 2);

        // если коллекция bids не будет загружена,
        // она не сможет стать отсоединенной
        em.detach(item);
    }

    @Test
    void cascadeDetachNewBid() throws Exception {
        UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        Assertions.assertEquals(item.getBids().size(), 2);
        em.detach(item);

        em.clear();
        item.setName("New Name");

        final Bid bid = new Bid(new BigDecimal("123.123"), item);
        item.getBids().add(bid);

        final Item mergeItem = em.merge(item);
        for (Bid b : mergeItem.getBids()) {
            Assertions.assertNotNull(b.getId());
        }

        em.flush();

        tx.commit();
        em.close();
    }

    @Test
    void cascadeCollection() {
        final EntityManager em = JPA.createEntityManager();
        final User user = em.find(User.class, createdItem.getId());

        Assertions.assertEquals(user.getBillingDetails().size(), 2);
        for (BillingDetails bd : user.getBillingDetails()) {
            Assertions.assertEquals(bd.getOwner(), "John Doe");
        }

        em.refresh(user);
    }
}
