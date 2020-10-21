package com.orm.hibernate.ex.model.entity.mapping.enums;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ItemEnums {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuctionType auctionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final ItemEnums item = new ItemEnums();
            item.setAuctionType(AuctionType.FIXED_BID);
            entityManager.persist(item);
        });
    }
}
