package com.orm.hibernate.jta.env.test;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

public class FetchTestLoadEventListener implements PostLoadEventListener {
    protected Map<Class, Integer> loadCount = new HashMap<>();

    public FetchTestLoadEventListener(EntityManagerFactory emf) {
        ServiceRegistry serviceRegistry =
                ((SessionFactoryImplementor) emf.unwrap(
                        org.hibernate.SessionFactory.class
                )).getServiceRegistry();
        serviceRegistry.getService(EventListenerRegistry.class)
                .appendListeners(EventType.POST_LOAD, this);
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Class entityType = event.getEntity().getClass();
        if (!loadCount.containsKey(entityType)) {
            loadCount.put(entityType, 0);
        }
        loadCount.put(entityType, loadCount.get(entityType) + 1);
    }

    public int getLoadCount(Class entityType) {
        return loadCount.containsKey(entityType) ? loadCount.get(entityType) : 0;
    }

    public void reset() {
        loadCount.clear();
    }
}