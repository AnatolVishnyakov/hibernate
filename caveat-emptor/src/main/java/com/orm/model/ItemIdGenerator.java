package com.orm.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class ItemIdGenerator {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
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

        ItemIdGenerator itemIdGenerator = new ItemIdGenerator();
        itemIdGenerator.setName("test name");
        em.persist(itemIdGenerator);

        tx.commit();
        em.close();
    }
}
