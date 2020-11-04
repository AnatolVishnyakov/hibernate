package com.orm.hibernate.ex.model.associations.onetoone.jointable;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;

    protected String name;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Shipment shipment = new Shipment();
            entityManager.persist(shipment);

            final Item item = new Item();
            entityManager.persist(item);
            final Shipment auctionShipment = new Shipment(item);
            entityManager.persist(auctionShipment);
        });
    }
}
