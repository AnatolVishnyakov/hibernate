package com.orm.hibernate.ex.model.associations.onetomany.orphan.remove;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    // удалить, эта связь не нужна,
    // лучше запросом доставать
    @OneToMany(mappedBy = "bidder")
    protected Set<Bid> bids = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }
}
