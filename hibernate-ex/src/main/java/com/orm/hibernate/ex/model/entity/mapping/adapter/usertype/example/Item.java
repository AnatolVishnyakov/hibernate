package com.orm.hibernate.ex.model.entity.mapping.adapter.usertype.example;

import com.orm.hibernate.ex.model.EntitySaver;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @NotNull
    @Type(type = "monetary_amount_usd")
    @Columns(columns = {
            @Column(name = "BUYNOWPRICE_AMOUNT"),
            @Column(name = "BUYNOWPRICE_CURRENCY", length = 3)
    })
    private MonetaryAmount buyNowPrice;
    @NotNull
    @Type(type = "monetary_amount_eur")
    @Columns(columns = {
            @Column(name = "INITIALPRICE_AMOUNT"),
            @Column(name = "INITIALPRICE_CURRENCY", length = 3)
    })
    private MonetaryAmount initialPrice;

    public MonetaryAmount getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(MonetaryAmount buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public MonetaryAmount getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(MonetaryAmount initialPrice) {
        this.initialPrice = initialPrice;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final Item item = new Item();
            item.setBuyNowPrice(new MonetaryAmount(new BigDecimal("11.23"), Currency.getInstance("USD")));
            item.setInitialPrice(new MonetaryAmount(new BigDecimal("146.52"), Currency.getInstance("EUR1")));
            entityManager.persist(item);
        });
    }
}
