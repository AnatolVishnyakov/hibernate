package com.orm.model.generator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ItemTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ItemTwoGeneratorId")
    private Long id;
}
