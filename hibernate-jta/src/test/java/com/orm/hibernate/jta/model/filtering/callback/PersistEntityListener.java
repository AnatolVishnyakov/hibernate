package com.orm.hibernate.jta.model.filtering.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;

public class PersistEntityListener {
    private static final Logger logger = LoggerFactory.getLogger(PersistEntityListener.class);

    // Превращает метод в Callback
    @PostPersist
    public void notifyAdmin(Object entityInstance) {
        logger.info("Callback notify persist entity");

        User currentUser = CurrentUser.INSTANCE.get();
        Mail mail = Mail.INSTANCE;

        mail.send(
            "Entity instance persisted by "
                + currentUser.getUsername()
                + ": "
                + entityInstance
        );
    }
}
