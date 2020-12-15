package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.interceptor.AuditLogRecord;
import com.orm.hibernate.jta.model.filtering.interceptor.Item;
import com.orm.hibernate.jta.model.filtering.interceptor.User;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuditLogging extends JPATest {
    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("FilteringInterceptorPU");
    }

    @Test
    public void writeAuditLog() throws Throwable {
        UserTransaction tx = TM.getUserTransaction();
        try {

            Long CURRENT_USER_ID;
            {
                tx.begin();
                EntityManager em = JPA.createEntityManager();
                User currentUser = new User("johndoe");
                em.persist(currentUser);
                tx.commit();
                em.close();
                CURRENT_USER_ID = currentUser.getId();
            }

            EntityManagerFactory emf = JPA.getEntityManagerFactory();

            Map<String, String> properties = new HashMap<>();
            properties.put(
                org.hibernate.cfg.AvailableSettings.SESSION_SCOPED_INTERCEPTOR,
                AuditLogInterceptor.class.getName()
            );

            EntityManager em = emf.createEntityManager(properties);

            Session session = em.unwrap(Session.class);
            final AuditLogInterceptor interceptor = (AuditLogInterceptor) ((SessionImplementor) session)
                    .getInterceptor();
            interceptor.setCurrentSession(session);
            interceptor.setCurrentUserId(CURRENT_USER_ID);

            tx.begin();
            em.joinTransaction();
            Item item = new Item("Foo");
            em.persist(item);
            tx.commit();
            em.clear();

            tx.begin();
            em.joinTransaction();
            List<AuditLogRecord> logs = em.createQuery(
                "select lr from AuditLogRecord lr",
                AuditLogRecord.class
            ).getResultList();
            assertEquals(logs.size(), 1);
            assertEquals(logs.get(0).getMessage(), "insert");
            assertEquals(logs.get(0).getEntityClass(), Item.class);
            assertEquals(logs.get(0).getEntityId(), item.getId());
            assertEquals(logs.get(0).getUserId(), CURRENT_USER_ID);
            em.createQuery("delete AuditLogRecord").executeUpdate();
            tx.commit();
            em.clear();

            tx.begin();
            em.joinTransaction();
            item = em.find(Item.class, item.getId());
            item.setName("Bar");
            tx.commit();
            em.clear();

            tx.begin();
            em.joinTransaction();
            logs = em.createQuery(
                "select lr from AuditLogRecord lr",
                AuditLogRecord.class
            ).getResultList();
            assertEquals(logs.size(), 1);
            assertEquals(logs.get(0).getMessage(), "update");
            assertEquals(logs.get(0).getEntityClass(), Item.class);
            assertEquals(logs.get(0).getEntityId(), item.getId());
            assertEquals(logs.get(0).getUserId(), CURRENT_USER_ID);
            tx.commit();
            em.close();

        } finally {
            TM.rollback();
        }
    }
}
