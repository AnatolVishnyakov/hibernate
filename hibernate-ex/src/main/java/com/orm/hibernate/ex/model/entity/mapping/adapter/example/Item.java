package com.orm.hibernate.ex.model.entity.mapping.adapter.example;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    private MonetaryAmount buyNowPrice;
    @Column
    @Convert(
            converter = MonetaryAmountConverter.class,
            disableConversion = true
    )
    private MonetaryAmount amount;

    public MonetaryAmount getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(MonetaryAmount buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final Item item = new Item();
            final MonetaryAmount amount = new MonetaryAmount(new BigDecimal("11.23"), Currency.getInstance("USD"));
            item.setBuyNowPrice(amount);
//            item.setAmount(amount);
            entityManager.persist(item);
        });
    }
}
