package com.orm.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class ItemAutoGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CaveatEmptor");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        ItemAutoGenerator itemIdGenerator = new ItemAutoGenerator();
        itemIdGenerator.setName("test name");
        em.persist(itemIdGenerator);

        tx.commit();
        em.close();
    }
}
