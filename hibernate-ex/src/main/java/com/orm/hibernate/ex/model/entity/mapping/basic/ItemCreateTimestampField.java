package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.QueryProcessor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

//@Entity
public class ItemCreateTimestampField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date lastModified;
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    @CreationTimestamp
    private Date createDate;
    @Temporal(TemporalType.TIME)
    @Column(updatable = false)
    @CreationTimestamp
    private Date createTime;
    @Column
    private String name;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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
            ItemCreateTimestampField item = new ItemCreateTimestampField();
            item.setName("test");
            entityManager.persist(item);
        });
    }
}
