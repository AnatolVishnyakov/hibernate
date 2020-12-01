package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.env.test.FetchTestLoadEventListener;
import com.orm.hibernate.jta.model.fetching.fetchloadgraph.Bid;
import com.orm.hibernate.jta.model.fetching.fetchloadgraph.Item;
import com.orm.hibernate.jta.model.fetching.fetchloadgraph.User;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Subgraph;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.*;

public class FetchLoadGraph extends JPATest {
    private Item createdItem;
    private FetchTestLoadEventListener loadEventListener;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingFetchLoadGraphPU");
    }

    @Override
    public void afterJPABootstrap() throws Exception {
        loadEventListener = new FetchTestLoadEventListener(JPA.getEntityManagerFactory());
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
    void loadGraphDefaultItem() {
        final EntityManager em = JPA.createEntityManager();
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", em.getEntityGraph(Item.class.getSimpleName()));

        final Item item = em.find(Item.class, createdItem.getId(), properties);
    }

    @Test
    void loadGraphCallApi() {
        final EntityManager em = JPA.createEntityManager();
        final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", itemGraph);

        Item item = em.find(Item.class, createdItem.getId(), properties);
    }

    @Test
    void loadGraphItemSeller() {
        final EntityManager em = JPA.createEntityManager();
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", em.getEntityGraph("ItemSeller"));

        final Item item = em.find(Item.class, createdItem.getId(), properties);
    }

    @Test
    void loadGraphItemSellerCallApi() {
        final EntityManager em = JPA.createEntityManager();
        final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
        // TODO поправить генерацию статической метамодели
//        itemGraph.addAttributeNodes(Item_.seller);

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", itemGraph);

        final Item item = em.find(Item.class, createdItem.getId(), properties);
    }

    @Test
    void loadGraphItemWithHint() {
        final EntityManager em = JPA.createEntityManager();
        final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
        // TODO поправить генерацию статической метамодели
//        itemGraph.addAttributeNodes(Item_.seller);

        final List<Item> items = em.createQuery("select i from Item i", Item.class)
                .setHint("javax.persistence.loadgraph", itemGraph)
                .getResultList();
    }

    @Test
    void loadGroupGraph() {
        final EntityManager em = JPA.createEntityManager();
        final EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
        // TODO поправить генерацию статической метамодели
//        bidGraph.addAttributeNodes(Bid_.bidder, Bid_.item);
//        Subgraph<Item> itemGraph = bidGraph.addSubgraph(Bid_.item);
//        itemGraph.addAttributeNodes(Item_.seller, Item_.bids);

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.loadgraph", bidGraph);

        final Bid bid = em.find(Bid.class, createdItem.getBids().iterator().next().getId(), properties);
    }
}
