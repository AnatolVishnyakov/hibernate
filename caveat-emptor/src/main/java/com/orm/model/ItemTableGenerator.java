package com.orm.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class ItemTableGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ItemGeneratorId")
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

    @Override
    public String toString() {
        return "ItemTableGenerator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}\n";
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CaveatEmptor");

        EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        tx.begin();

        ItemTableGenerator itemTableGenerator = new ItemTableGenerator();
        itemTableGenerator.setName("test name");
        em.persist(itemTableGenerator);

        tx.commit();

        final List result = em.createQuery("SELECT i FROM ItemTableGenerator i")
                .getResultList();
        System.out.println(result);

        em.close();
    }
}
