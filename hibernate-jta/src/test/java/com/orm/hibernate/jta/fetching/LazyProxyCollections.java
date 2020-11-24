package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.proxy.Bid;
import com.orm.hibernate.jta.model.proxy.Item;
import com.orm.hibernate.jta.model.proxy.User;
import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.proxy.HibernateProxyHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LazyProxyCollections extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingProxyPU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();
        final User seller = new User("test-user");
        em.persist(seller);
        createdItem = new Item("Original Name", new Date(), seller);
        em.persist(createdItem);

        for (int i = 1; i <= 3; i++) {
            Bid bid = new Bid(createdItem, seller, BigDecimal.valueOf(new Random().nextDouble()));
            createdItem.getBids().add(bid);
            em.persist(bid);
        }

        tx.commit();
        em.close();
    }

    @Test
    void whenProxyObject() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.getReference(Item.class, createdItem.getId());

        // получим прокси-объект, select
        // запроса к БД не будет (только для getId)
        assertEquals(item.getId(), createdItem.getId());
        // будет select вызов к БД
//        assertEquals(item.getName(), createdItem.getName());
        assertNotEquals(item.getClass(), Item.class);
        assertEquals(HibernateProxyHelper.getClassWithoutInitializingProxy(item), Item.class);
    }

    @Test
    void isLoadedEntity() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.getReference(Item.class, createdItem.getId());

        final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertFalse(persistenceUtil.isLoaded(item));
        assertFalse(persistenceUtil.isLoaded(item, "bids"));
        assertFalse(Hibernate.isInitialized(item));
//        assertFalse(Hibernate.isInitialized(item.getBids())); // Вызовет инициализацию item
    }

    @Test
    void onInitialize() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.getReference(Item.class, createdItem.getId());

        Hibernate.initialize(item);
        assertFalse(Hibernate.isInitialized(item.getBids()));
    }

    @Test
    void exceptionLazyInitialization() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());

        em.detach(item);
        em.detach(item.getSeller());

        final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertTrue(persistenceUtil.isLoaded(item));
        assertFalse(persistenceUtil.isLoaded(item, "seller"));
        assertEquals(item.getSeller().getId(), createdItem.getSeller().getId());
        // бросит исключение, т.к. в отсоединенном
        // объекте доступен только метод чтения
        // идентификатора
//        assertNotNull(item.getSeller().getUsername());
    }

    @Test
    void saveNewBidNotLoadedInMemory() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.getReference(Item.class, createdItem.getId());
        final User user = em.getReference(User.class, createdItem.getSeller().getId());

        final Bid bid = new Bid(new BigDecimal("131.01"));
        bid.setItem(item);
        bid.setBidder(user);
        em.persist(bid);

        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();
        em.joinTransaction();
        tx.commit();
        em.close();
    }

    @Test
    void loadLazyCollection() {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        final Set<Bid> bids = item.getBids();

        final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertFalse(persistenceUtil.isLoaded(item, "bids"));
        assertTrue(Set.class.isAssignableFrom(bids.getClass()));
        assertNotEquals(bids.getClass(), HashSet.class);
        assertEquals(bids.getClass(), PersistentSet.class);
    }

    @Test
    void operationsLazyCollection() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        final EntityManager em = JPA.createEntityManager();
        final Item item = em.find(Item.class, createdItem.getId());
        assertEquals(item.getBids().size(), 3);
        assertFalse(item.getBids().isEmpty());
        assertFalse(item.getBids().contains(new Bid(new BigDecimal(1))));
    }
}
