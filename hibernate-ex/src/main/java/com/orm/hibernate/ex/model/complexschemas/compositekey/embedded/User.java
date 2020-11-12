package com.orm.hibernate.ex.model.complexschemas.compositekey.embedded;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
@Table(name = "USERS")
public class User {
    @EmbeddedId
    protected UserId id;

    public User() {
    }

    public User(UserId id) {
        this.id = id;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final UserId userId = new UserId("test", "dep-1");
            final User user = new User(userId);
            entityManager.persist(user);
        });
    }
}
