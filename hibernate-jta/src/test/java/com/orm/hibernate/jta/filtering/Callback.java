package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.callback.CurrentUser;
import com.orm.hibernate.jta.model.filtering.callback.Item;
import com.orm.hibernate.jta.model.filtering.callback.Mail;
import com.orm.hibernate.jta.model.filtering.callback.User;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Callback extends JPATest {
    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringCallbackPU");
    }

    @Test
    void callbackPostPersist() throws Exception {
        UserTransaction tx = TM.getUserTransaction();
        try {
            tx.begin();
            EntityManager em = JPA.createEntityManager();

            {
                User user = new User("johndoe");
                CurrentUser.INSTANCE.set(user); // Thread-local

                em.persist(user);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                assertTrue(Mail.INSTANCE.get(0).contains("johndoe"));
                Mail.INSTANCE.clear();


                Item item = new Item("Foo", user);
                em.persist(item);
                assertEquals(Mail.INSTANCE.size(), 0);
                em.flush();
                assertEquals(Mail.INSTANCE.size(), 1);
                assertTrue(Mail.INSTANCE.get(0).contains("johndoe"));
                Mail.INSTANCE.clear();

                CurrentUser.INSTANCE.set(null);
            }

            em.clear();

        } finally {
            TM.rollback();
        }
    }
}
