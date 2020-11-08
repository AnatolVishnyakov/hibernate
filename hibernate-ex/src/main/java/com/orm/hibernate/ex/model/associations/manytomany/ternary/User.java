package com.orm.hibernate.ex.model.associations.manytomany.ternary;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    @NotNull
    private String userName;

    public User() {
    }

    public User(@NotNull String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
