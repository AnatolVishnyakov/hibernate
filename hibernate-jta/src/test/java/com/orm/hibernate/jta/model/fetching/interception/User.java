package com.orm.hibernate.jta.model.fetching.interception;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
