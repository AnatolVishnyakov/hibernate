package com.orm.hibernate.ex.model.inheritance.singletable;

import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "BD_TYPE")
//@DiscriminatorFormula("case when CARDNUMBER is not null then 'CC' else 'BA' end")
public abstract class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
