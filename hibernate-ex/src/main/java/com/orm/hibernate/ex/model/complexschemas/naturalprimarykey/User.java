package com.orm.hibernate.ex.model.complexschemas.naturalprimarykey;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
@Table(name = "USERS")
public class User {
    // Естественный первичный ключ
    @Id
    protected String username;

    protected User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final User user = new User("test");
            entityManager.persist(user);
        });
    }
}