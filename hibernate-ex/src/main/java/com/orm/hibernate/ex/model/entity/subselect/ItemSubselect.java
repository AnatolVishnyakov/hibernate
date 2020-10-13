package com.orm.hibernate.ex.model.entity.subselect;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
@Immutable
@Subselect(
        value = "select i1.ID, i1.NAME " +
                "from ItemOne i1 left join ItemTwo i2 on i1.itemtwo_id = i2.id"
)
@Synchronize(value = {"ItemOne", "ItemTwo"})
public class ItemSubselect {
    @Id
    private Long id;
    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ItemSubselect{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
