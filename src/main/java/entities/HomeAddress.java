package entities;

import javax.persistence.Embeddable;

@Embeddable
public class HomeAddress {
    private String street;

    public HomeAddress(String street) {
        this.street = street;
    }

    public HomeAddress() {
    }

    public String getStreet() {
        return street;
    }
}
