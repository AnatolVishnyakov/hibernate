package com.orm.hibernate.ex.model.complexschemas.custom;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column(
            nullable = false,                       // ограничение столбца
            unique = true,                          // таблицное ограничение
            columnDefinition = "EMAIL_ADDRESS(255)" // ограничение домена
    )
    protected String email;

    protected String username;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}