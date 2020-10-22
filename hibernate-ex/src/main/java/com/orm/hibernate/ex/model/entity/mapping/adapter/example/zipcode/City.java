package com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class City {
    @NotNull
    @Column(nullable = false)
    protected ZipCode zipCode;
    @NotNull
    @Column(nullable = false)
    protected String country;
    @NotNull
    @Column(nullable = false)
    protected String name;

    public City() {
    }

    public City(String name, String country, ZipCode zipCode) {
        this.name = name;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String city) {
        this.country = city;
    }
}
