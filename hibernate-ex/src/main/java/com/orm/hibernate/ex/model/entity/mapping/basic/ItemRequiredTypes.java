package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Entity
public class ItemRequiredTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Basic(optional = false)
    private String firstName;
    @Column(nullable = false)
    private String secondName;
    @NotNull(message = "Field not be null")
    private String thirdName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            ItemRequiredTypes item = new ItemRequiredTypes();
            item.setFirstName("first name");
            item.setSecondName("second name");
            item.setThirdName(null);
            entityManager.persist(item);
        });
    }
}
