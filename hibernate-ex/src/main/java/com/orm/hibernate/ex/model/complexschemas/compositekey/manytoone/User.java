package com.orm.hibernate.ex.model.complexschemas.compositekey.manytoone;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
@Table(name = "USERS")
public class User {
    @EmbeddedId
    private UserId id;

    public User(UserId id) {
        this.id = id;
    }

    public User() {
    }

    public UserId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}