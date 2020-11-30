package com.orm.hibernate.jta.fetching;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.fetching.profile.Item;
import com.orm.hibernate.jta.model.fetching.profile.Bid;
import com.orm.hibernate.jta.model.fetching.profile.User;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Profile extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FetchingProfilePU");
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
    void callProfile() {
        final EntityManager em = JPA.createEntityManager();
        Item item = em.find(Item.class, createdItem.getId());

        em.clear();
        em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_SELLER);

        item = em.find(Item.class, createdItem.getId());

        em.clear();
        em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_BIDS);

        item = em.find(Item.class, createdItem.getId());
    }
}
