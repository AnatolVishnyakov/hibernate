package com.orm.hibernate.ex.model.associations.onetoone.jointable;

import javax.persistence.*;

//@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    // Рекомендуется использовать, если
    // связь один к одному является
    // необязательной
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ITEM_SHIPMENT",
            joinColumns = @JoinColumn(name = "SHIPMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID", nullable = false, unique = true)
    )
    private Item auctionId;

    public Shipment() {
    }

    public Shipment(Item auctionId) {
        this.auctionId = auctionId;
    }
}
