package com.orm.hibernate.ex.model.entity.mapping.adapter;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.Type;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Type(type = "yes_no")
    private boolean verified = false;

    public Item() {
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", verified=" + verified +
                '}';
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            final Item item = new Item();
            item.setVerified(true);
            entityManager.persist(item);
        });
    }
}
