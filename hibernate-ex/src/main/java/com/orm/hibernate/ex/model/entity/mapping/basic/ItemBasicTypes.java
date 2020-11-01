package com.orm.hibernate.ex.model.entity.mapping.basic;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

//@Entity
public class ItemBasicTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // создает таблицу hibernate_sequence
    protected Long id;
    private String string;
    private BigInteger bigInteger;
    private BigDecimal bigDecimal;
    private Date date;
    private Calendar calendar;
    private java.sql.Date dateSql;
    private Time timeSql;
    private Timestamp timestampSql;
    private byte[] byteArray;
    private Byte[] bytes;
    private char[] aChar;
    private Character[] characters;

    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
            entityManager.persist(new ItemBasicTypes());
        });
    }
}
