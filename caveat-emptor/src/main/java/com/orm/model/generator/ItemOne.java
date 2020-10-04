package com.orm.model.generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ItemOne {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ItemOneGeneratorId")
    private Long id;
}
