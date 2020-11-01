package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

//@Entity
public class ItemFieldGenerated {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    @ColumnDefault(value = "2020-10-21T20:48:39.478")
    @org.hibernate.annotations.Generated(
            GenerationTime.ALWAYS
    )
    private Date lastModified;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            ItemFieldGenerated item = new ItemFieldGenerated();
            item.setName("test");
            item.setLastModified(new Date());
            entityManager.persist(item);
        });
    }
}
