package com.orm.hibernate.ex.model.inheritance.associations.onetomany;

import javax.persistence.*;

// Не может иметь аннотацию @MappedSuperclass
//@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public BillingDetails() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
