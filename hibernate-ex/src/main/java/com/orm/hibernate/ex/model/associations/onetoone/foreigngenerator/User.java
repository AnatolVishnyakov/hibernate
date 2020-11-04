package com.orm.hibernate.ex.model.associations.onetoone.foreigngenerator;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Random;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.PERSIST
    )
    private Address shippingAddress;
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public static void main(String[] args) {
        final User user = new User("OneUser-" + new Random().nextInt(10_000));
        QueryProcessor.process(entityManager -> {
            // Связывать нужно обе стороны
            Address address = new Address(user, "Green St.", "12345", "New York"); // первая связь
            user.setShippingAddress(address); // вторая связь
            entityManager.persist(user);
        });

        QueryProcessor.process(entityManager -> {
//            final User u = entityManager.find(User.class, user.id);
//            System.out.println(u.getShippingAddress());
            final Address address = entityManager.find(Address.class, user.id);
//            System.out.println(address.getUser());
        });
    }
}
