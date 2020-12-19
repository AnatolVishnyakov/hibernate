package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.env.JPATest;
import com.orm.hibernate.jta.model.filtering.envers.Item;
import com.orm.hibernate.jta.model.filtering.envers.User;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

            final Item item = new Item("Foo", user);
            em.persist(item);

            tx.commit();
            em.close();

            ITEM_ID = item.getId();
            USER_ID = user.getId();
        }
        Date TIMESTAMP_CREATE = new Date();

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
        Date TIMESTAMP_UPDATE = new Date();

        {
            // delete
            tx.begin();
            final EntityManager em = JPA.createEntityManager();

            final Item item = em.find(Item.class, ITEM_ID);
            em.remove(item);

            tx.commit();
            em.close();
        }
        Date TIMESTAMP_DELETE = new Date();

        final EntityManager em = JPA.createEntityManager();
        AuditReader auditReader = AuditReaderFactory.get(em);
        {
            // search
            final Number revCreate = auditReader.getRevisionNumberForDate(TIMESTAMP_CREATE);
            final Number revUpdate = auditReader.getRevisionNumberForDate(TIMESTAMP_UPDATE);
            final Number revDelete = auditReader.getRevisionNumberForDate(TIMESTAMP_DELETE);

            final List<Number> itemRevisions = auditReader.getRevisions(Item.class, ITEM_ID);
            assertEquals(itemRevisions.size(), 3);

            for (Number itemRevision : itemRevisions) {
                final Date revisionDate = auditReader.getRevisionDate(itemRevision);
                System.out.println(itemRevision + " : " + revisionDate);
            }

            final List<Number> userRevisions = auditReader.getRevisions(User.class, USER_ID);
            assertEquals(userRevisions.size(), 2);

            final AuditQuery query = auditReader.createQuery()
                    .forRevisionsOfEntity(Item.class, false, false);

            final List<Object[]> result = query.getResultList();
            for (Object[] tuple : result) {
                final Item item = (Item) tuple[0];
                final DefaultRevisionEntity revision = (DefaultRevisionEntity) tuple[1];
                final RevisionType revisionType = (RevisionType) tuple[2];

                if (revision.getId() == 3) {
                    assertEquals(RevisionType.ADD, revisionType);
                    assertEquals("Foo", item.getName());
                } else if (revision.getId() == 2) {
                    assertEquals(RevisionType.MOD, revisionType);
                    assertEquals("Bar", item.getName());
                } else if (revision.getId() == 1) {
                    assertEquals(RevisionType.DEL, revisionType);
                    assertNull(item);
                }
            }

            // get archive
            final Item createdItem = auditReader.find(Item.class, ITEM_ID, revCreate);
            assertEquals("Foo", createdItem.getName());
            assertEquals("johndoe", createdItem.getSeller().getUsername());

            final Item updatedItem = auditReader.find(Item.class, ITEM_ID, revUpdate);
            assertEquals("Bar", updatedItem.getName());
            assertEquals("doejohn", updatedItem.getSeller().getUsername());

            final Item deletedItem = auditReader.find(Item.class, ITEM_ID, revDelete);
            assertNull(deletedItem);

            final User user = auditReader.find(User.class, USER_ID, revDelete);
            assertEquals("doejohn", user.getUsername());
        }
    }
}
