package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.EntitySaver;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ItemUpdateTimestampField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @UpdateTimestamp
    private Date lastModified;
    private String name;
    private static int counter = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final long count = entityManager
                    .createQuery("select count(i.id) from ItemUpdateTimestampField i", Long.class)
                    .getSingleResult();
            if (count == 0) {
                final ItemUpdateTimestampField item = new ItemUpdateTimestampField();
                item.setName("test");
                entityManager.persist(item);
            }
        });

        EntitySaver.save(entityManager -> {
            final ItemUpdateTimestampField item = entityManager
                    .createQuery("select i from ItemUpdateTimestampField i", ItemUpdateTimestampField.class)
                    .setMaxResults(1)
                    .getSingleResult();
            item.setName(item.getName() + counter++);
            entityManager.persist(item);
        });
    }
}
