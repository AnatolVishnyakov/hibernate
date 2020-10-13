package com.orm.hibernate.ex.model.entity.naming.two;

import javax.persistence.*;

//@Entity(name = "AuctionItem")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает sequence hibernate_sequence
    protected Long id;

    public Long getId() {
        return id;
    }
}
