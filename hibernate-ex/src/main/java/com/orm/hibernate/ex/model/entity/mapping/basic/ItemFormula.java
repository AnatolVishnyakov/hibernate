package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.Formula;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class ItemFormula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Formula("select i from Item i where i.id = 24")
    protected String shortDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            ItemFormula item = new ItemFormula();
            entityManager.persist(item);
            System.out.println(item.getShortDescription());
        });
    }
}
