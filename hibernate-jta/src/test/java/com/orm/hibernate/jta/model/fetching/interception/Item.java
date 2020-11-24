package com.orm.hibernate.jta.model.fetching.interception;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    protected String name;
    @NotNull
    protected Date auctionEnd;
    @NotNull
    // Lazy не будет работать,
    // т.к. для User отключен прокси
    @ManyToOne(fetch = FetchType.LAZY)
    // Говорит оптимизатору байт кода
    // вызывать загрузку при обращении
    @org.hibernate.annotations.LazyToOne(
            org.hibernate.annotations.LazyToOneOption.NO_PROXY
    )
    protected User seller;

    @NotNull
    @Length(max = 4000)
    @Basic(fetch = FetchType.LAZY)
    protected String description;

    public Item() {
    }

    public Item(String name, Date auctionEnd, User seller, String description) {
        this.name = name;
        this.auctionEnd = auctionEnd;
        this.seller = seller;
        this.description = description;
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

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
