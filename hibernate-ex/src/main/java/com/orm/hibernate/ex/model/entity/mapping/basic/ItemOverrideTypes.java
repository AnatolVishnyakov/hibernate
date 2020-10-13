package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.*;

@Entity
public class ItemOverrideTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    private String name;
    @Transient
    private boolean flag;
    @Basic(optional = false)
    private boolean test;

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

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            final ItemOverrideTypes entity = new ItemOverrideTypes();
            entity.setName("test");
            entityManager.persist(entity);
        });
    }
}
