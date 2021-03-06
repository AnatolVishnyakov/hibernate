package com.orm.hibernate.jta.model.filtering.dynamic;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected String username;
    @NotNull
    protected int rank = 0;

    protected User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, int rank) {
        this.username = username;
        this.rank = rank;
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

    public int getRank() {
        return rank;
    }
}
