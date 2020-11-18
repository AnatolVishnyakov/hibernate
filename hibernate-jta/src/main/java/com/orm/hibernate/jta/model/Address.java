package com.orm.hibernate.jta.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

    @NotNull // Игнорируется при DDL генерации!
    @Column(nullable = false) // Используется при DDL генерации!
    protected String street;

    @NotNull
    @Column(nullable = false, length = 5) // Переопределение VARCHAR(255)
    protected String zipcode;

    @NotNull
    @Column(nullable = false)
    protected String city;

    protected Address() {
    }

    public Address(String street, String zipcode, String city) {
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
