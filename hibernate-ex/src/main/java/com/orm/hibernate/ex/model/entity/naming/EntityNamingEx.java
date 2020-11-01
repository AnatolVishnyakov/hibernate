package com.orm.hibernate.ex.model.entity.naming;

import com.orm.hibernate.ex.model.QueryProcessor;
import com.orm.hibernate.ex.model.entity.naming.two.Item;

public class EntityNamingEx {
    public static void main(String[] args) {
        QueryProcessor.process(em -> {
            em.persist(new Item());
            em.persist(new com.orm.hibernate.ex.model.entity.naming.one.Item());
        });
    }
}
