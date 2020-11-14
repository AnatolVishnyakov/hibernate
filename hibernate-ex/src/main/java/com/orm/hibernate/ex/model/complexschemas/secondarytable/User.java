package com.orm.hibernate.ex.model.complexschemas.secondarytable;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Random;

//@Entity
@Table(name = "USERS")
// рекомендуется использовать
// только в неизменяемых legacy схемах
@SecondaryTable(
        name = "BILLING_ADDRESS",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "USER_ID")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Address homeAddress;
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(table = "BILLING_ADDRESS", nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(table = "BILLING_ADDRESS", length = 5, nullable = false)),
            @AttributeOverride(name = "city", column = @Column(table = "BILLING_ADDRESS", nullable = false))
    })
    private Address billingAddress;

    public User() {
    }

    public User(@NotNull String name, @NotNull Address homeAddress) {
        this.name = name;
        this.homeAddress = homeAddress;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public static void main(String[] args) {
        final int genId = new Random().nextInt(10_000);
        QueryProcessor.process(entityManager -> {
            final Address billingAddress = new Address("Time Square", "42343", "New York");
            final Address homeAddress = new Address("Green St.", "12345", "New York");
            final User user = new User("User #" + genId, homeAddress);
            user.setBillingAddress(billingAddress);
            entityManager.persist(user);
        });
    }
}
