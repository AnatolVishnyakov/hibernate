package entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
@Access(AccessType.FIELD)
public class Address {
    private transient String city;
    @Transient
    private String street;
    private String house;

    public Address() {
        // Обязательный конструктор в Embeddable классе
    }

    public Address(String city, String street, String house) {
        this.city = city;
        this.street = street;
        this.house = house;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                '}';
    }
}
