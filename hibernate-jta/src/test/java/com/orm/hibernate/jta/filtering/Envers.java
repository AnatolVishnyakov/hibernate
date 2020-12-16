package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.envers.Item;
import com.orm.hibernate.jta.model.filtering.envers.User;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;

public class Envers extends JPATest {
    @Override
    public void configurePersistenceUnit() throws Exception {
        super.configurePersistenceUnit("FilteringEnversPU");
        System.setProperty("keepSchema", "true");
    }

    @Test
    void auditLogging() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        final UserTransaction tx = TM.getUserTransaction();

        Long ITEM_ID;
        Long USER_ID;

        {
            // create
            tx.begin();
            final EntityManager em = JPA.createEntityManager();

            final User user = new User("johndoe");
            em.persist(user);

            final Item item = new Item("test", user);
            em.persist(item);

            tx.commit();
            em.close();

            ITEM_ID = item.getId();
            USER_ID = user.getId();
        }

        {
            // update
            tx.begin();
            final EntityManager em = JPA.createEntityManager();

            final Item item = em.find(Item.class, ITEM_ID);
            item.setName("Bar");
            item.getSeller().setUsername("doejohn");

            tx.commit();
            em.close();
        }

        {
            // delete
            tx.begin();
            final EntityManager em = JPA.createEntityManager();

            final Item item = em.find(Item.class, ITEM_ID);
            em.remove(item);

            tx.commit();
            em.close();
        }
    }
}
