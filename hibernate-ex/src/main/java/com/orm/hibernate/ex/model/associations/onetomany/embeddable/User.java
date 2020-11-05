package com.orm.hibernate.ex.model.associations.onetomany.embeddable;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.*;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    private String username;
    private Address shippingAddress;

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

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final User user = new User("test-user-" + new Random().nextInt(10_000));
            Address address = new Address("Green St.", "21345", "London");
            user.setShippingAddress(address);
            entityManager.persist(user);

            Shipment shipment = new Shipment();
            shipment.setCreatedOn(new Date());
            address.getDeliveries().add(shipment);
            entityManager.persist(shipment);
        });
    }
}
