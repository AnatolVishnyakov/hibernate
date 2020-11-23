package com.orm.hibernate.jta.concurrency;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonTransactional extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("HibernateEx");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        createdItem = new Item("Original Name");
        em.persist(createdItem);

        tx.commit();
        em.close();
    }

    @Test
    void readWhenAutoCommitMode() {
        final EntityManager em = JPA.createEntityManager();

        final Item item = em.find(Item.class, createdItem.getId());
        item.setName("New Name");

        assertEquals(
                em.createQuery("select i.name from Item i where i.id = :id")
                        .setParameter("id", createdItem.getId())
                        .getSingleResult(),
                "Original Name"
        );

        assertEquals(
                ((Item) em.createQuery("select i from Item i where i.id = :id")
                        .setParameter("id", item.getId())
                        .getSingleResult()).getName(),
                "New Name"
        );

        // Нельзя выполнять UPDATE в
        // рассинхронизированном состоянии
//        em.flush();
        em.refresh(item);
        assertEquals(item.getName(), "Original Name");

        em.close();
    }
}
