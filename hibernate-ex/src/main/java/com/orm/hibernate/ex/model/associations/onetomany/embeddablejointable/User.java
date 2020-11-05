package com.orm.hibernate.ex.model.associations.onetomany.embeddablejointable;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    public Long getId() {
        return id;
    }

    protected String username;

    protected Address shippingAddress;

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

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final User user = new User("test-user-" + new Random().nextInt(10_000));
            Address address = new Address("Green St.", "52343", "New York");
            user.setShippingAddress(address);
            entityManager.persist(user);

            final Shipment shipment = new Shipment();
            shipment.setCreatedOn(new Date());
            address.getDeliveries().add(shipment);
            entityManager.persist(shipment);
        });
    }
}