package com.orm.hibernate.ex.model.inheritance.associations.onetomany;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column(name = "username")
    private String userName;
    @OneToMany(mappedBy = "user")
    private Set<BillingDetails> billingDetails = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<BillingDetails> getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(Set<BillingDetails> billingDetails) {
        this.billingDetails = billingDetails;
    }

    public void addBillingDetails(BillingDetails billingDetails) {
        this.billingDetails.add(billingDetails);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", billingDetails=" + billingDetails +
                '}';
    }
}
