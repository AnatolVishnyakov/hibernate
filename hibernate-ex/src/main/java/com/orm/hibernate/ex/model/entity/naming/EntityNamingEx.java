package com.orm.hibernate.ex.model.entity.naming;

import com.orm.hibernate.ex.model.EntitySaver;
import com.orm.hibernate.ex.model.entity.naming.two.Item;

public class EntityNamingEx {
    public static void main(String[] args) {
        EntitySaver.save(em -> {
            em.persist(new Item());
            em.persist(new com.orm.hibernate.ex.model.entity.naming.one.Item());
        });
    }
}
