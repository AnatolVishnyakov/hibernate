package com.orm.hibernate.ex.model.associations.onetoone.foreigngenerator;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Entity
public class Address {
    @Id
    @GeneratedValue(generator = "addressKeyGenerator") // создает таблицу hibernate_sequence
    @GenericGenerator(
            name = "addressKeyGenerator",
            strategy = "foreign",
            parameters = @Parameter(
                    name = "property", value = "user"
            )
    )
    private Long id;
    @OneToOne(optional = true)
    @PrimaryKeyJoinColumn
    private User user;
    @NotNull
    private String street;
    @NotNull
    private String zipcode;
    @NotNull
    private String city;

    public Address() {
    }

    public Address(User user) {
        this.user = user;
    }

    public Address(User user, String street, String zipcode, String city) {
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}