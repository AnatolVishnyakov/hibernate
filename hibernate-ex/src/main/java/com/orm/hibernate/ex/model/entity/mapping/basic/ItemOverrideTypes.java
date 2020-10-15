package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;

//@Entity
public class ItemOverrideTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    private String name;
    @Transient
    private boolean flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final ItemOverrideTypes entity = new ItemOverrideTypes();
            entityManager.persist(entity);
        });
    }
}
