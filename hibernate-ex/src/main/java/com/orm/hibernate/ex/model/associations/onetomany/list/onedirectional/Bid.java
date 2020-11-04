package com.orm.hibernate.ex.model.associations.onetomany.list.onedirectional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    protected BigDecimal amount;

    public Bid() {
    }

    public Bid(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
