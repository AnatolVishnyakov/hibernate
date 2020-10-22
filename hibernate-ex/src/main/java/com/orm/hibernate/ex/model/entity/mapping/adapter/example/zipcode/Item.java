package com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode;

import com.orm.hibernate.ex.model.EntitySaver;
import com.orm.hibernate.ex.model.entity.mapping.adapter.converter.ZipCodeConverter;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Convert(
            converter = ZipCodeConverter.class,
            attributeName = "city.zipCode"
    )
    private Address homeAddress;

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
//            final Item item = new Item();
//            Address address = new Address();
//            City city = new City("New York", "USA", new SwissZipCode("1234"));
//            address.setCity(city);
//            address.setStreet("Green St.");
//            item.setHomeAddress(address);
//            entityManager.persist(item);

            final Item item1 = entityManager.find(Item.class, 61L);
            final Item item2 = entityManager.find(Item.class, 62L);

            System.out.println(item1.getHomeAddress().getCity().getZipCode());
            System.out.println(item2.getHomeAddress().getCity().getZipCode());
        });
    }
}
