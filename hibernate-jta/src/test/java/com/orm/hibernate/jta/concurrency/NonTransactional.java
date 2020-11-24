package com.orm.hibernate.jta.concurrency;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void saveWhenNotSyncStateEntityManager() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        final EntityManager em = JPA.createEntityManager();

        final Item item = new Item("New Item");
        em.persist(item);
        assertNotNull(item.getId());

        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        if (!em.isJoinedToTransaction()) {
            // Сообщает EntityManager'у, что транзакция
            // JTA активна, и присоединяет к ней
            // контекст хранения.
            // Этот метод следует вызывать в диспетчере
            // управляемых объектов приложения JTA,
            // который был создан вне области действия
            // активной транзакции, или в диспетчере сущностей
            // типа SynchronizationType.UNSYNCHRONIZED,
            // чтобы связать его с текущей транзакцией JTA.
            em.joinTransaction();
        }

        tx.commit();
        em.close();
    }

    @Test
    void mergeWhenNotSyncStateEntityManager() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        createdItem.setName("New Name");

        final EntityManager em = JPA.createEntityManager();
        final Item mergedItem = em.merge(createdItem);

        final UserTransaction tx = TM.getUserTransaction();

        tx.begin();
        em.joinTransaction();
        tx.commit();
        em.close();
    }

    @Test
    void removeWhenNotSyncStateEntityManager() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        final EntityManager em = JPA.createEntityManager();

        final Item item = em.find(Item.class, createdItem.getId());
        em.remove(item);

        final UserTransaction tx = TM.getUserTransaction();

        tx.begin();
        em.joinTransaction();
        tx.commit();
        em.close();
    }
}
