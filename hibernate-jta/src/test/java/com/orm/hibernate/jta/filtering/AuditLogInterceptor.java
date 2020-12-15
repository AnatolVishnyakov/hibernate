package com.orm.hibernate.jta.filtering;

import com.orm.hibernate.jta.model.filtering.interceptor.AuditLogRecord;
import com.orm.hibernate.jta.model.filtering.interceptor.Auditable;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AuditLogInterceptor extends EmptyInterceptor {
    protected Session currentSession;
    protected Long currentUserId;
    protected Set<Auditable> inserts = new HashSet<>();
    protected Set<Auditable> updates = new HashSet<>();

    public void setCurrentSession(Session session) {
        this.currentSession = session;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) throws CallbackException {

        if (entity instanceof Auditable) {
            inserts.add((Auditable) entity);
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) throws CallbackException {

        if (entity instanceof Auditable) {
            updates.add((Auditable) entity);
        }
        return false;
    }

    @Override
    public void postFlush(Iterator iterator) throws CallbackException {
        try (Session tempSession = currentSession.sessionWithOptions().transactionContext().connection().openSession()) {
            for (Auditable entity : inserts) {
                tempSession.persist(
                        new AuditLogRecord("insert", entity, currentUserId)
                );
            }

            for (Auditable entity : updates) {
                tempSession.persist(
                        new AuditLogRecord("update", entity, currentUserId)
                );
            }

            tempSession.flush();
        } finally {
            inserts.clear();
            updates.clear();
        }
    }
}
