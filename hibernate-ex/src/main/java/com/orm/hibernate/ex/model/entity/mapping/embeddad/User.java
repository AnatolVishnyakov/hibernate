package com.orm.hibernate.ex.model.entity.mapping.embeddad;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    protected Address homeAddress;
    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
//            @AttributeOverride(name = "zipCode", column = @Column(name = "BILLING_ZIPCODE", length = 5)),
//            @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY"))
//    })
//    protected Address billingAddress;

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", homeAddress=" + homeAddress +
                '}';
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            User user = new User();
            Address address = new Address();
            address.setStreet("Test St.");
            final City city = new City("New York", "USA", "12345");
            address.setCity(city);
            user.setHomeAddress(address);
            entityManager.persist(user);
        });
    }
}
