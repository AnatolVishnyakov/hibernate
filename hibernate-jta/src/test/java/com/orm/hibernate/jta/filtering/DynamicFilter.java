package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.dynamic.*;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicFilter extends JPATest {
    private Item createdItem;

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringDynamicPU");
    }

    @BeforeEach
    void initDataSet() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        final UserTransaction tx = TM.getUserTransaction();
        tx.begin();

        final EntityManager em = JPA.createEntityManager();

        Category category = new Category("category-" + UUID.randomUUID().toString());
        em.persist(category);

        User seller = new User("user-" + UUID.randomUUID().toString(), 110);
        em.persist(seller);

        final Item item = new Item("test", category, seller);
        em.persist(item);

        User seller2 = new User("user-" + UUID.randomUUID().toString(), 10);
        em.persist(seller2);

        final Item item2 = new Item("test", category, seller2);
        em.persist(item2);

        tx.commit();
        em.close();
    }

    @Test
    void filterItems() {
        final EntityManager em = JPA.createEntityManager();
        {
            final Filter filter = em.unwrap(Session.class)
                    .enableFilter("limitByUserRank");
            filter.setParameter("currentUserRank", 11);

            {
                List<Item> items = em.createQuery("select i from Item i", Item.class)
                        .getResultList();
                // select * from ITEM where 0 >=
                //  (select u.RANK from USERS u  where u.ID = SELLER_ID)
                assertEquals(1, items.size());
            }
            em.clear();
        }
    }
}
