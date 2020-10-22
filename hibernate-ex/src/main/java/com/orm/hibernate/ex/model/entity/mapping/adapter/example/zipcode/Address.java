package com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {
    @NotNull
    @Column(nullable = false)
    protected String street;
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(
                    name = "name",
                    column = @Column(name = "CITY", nullable = false)
            )
    })
    protected City city;

    public Address() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}