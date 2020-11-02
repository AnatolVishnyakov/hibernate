package com.orm.hibernate.ex.model.collections.embeddable.embeddablesetofstrings;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    protected String username;
    protected Address address;

    public User() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            User user = new User("ololo-" + new Random().nextInt(10_000));
            Address address = new Address("Green St.", "23432", "USA");
            address.setContacts(new HashSet<>(Arrays.asList("contact-1", "contact-2", "contact-3")));
            user.setAddress(address);
            entityManager.persist(user);
        });
    }
}