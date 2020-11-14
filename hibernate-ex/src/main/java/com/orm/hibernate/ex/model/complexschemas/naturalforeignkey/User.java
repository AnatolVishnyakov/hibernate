package com.orm.hibernate.ex.model.complexschemas.naturalforeignkey;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//@Entity
@Table(name = "USERS")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    @Column(unique = true)
    protected String customerNr;

    protected User() {
    }

    public User(String customerNr) {
        this.customerNr = customerNr;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerNr() {
        return customerNr;
    }

    public void setCustomerNr(String customerNr) {
        this.customerNr = customerNr;
    }
}
