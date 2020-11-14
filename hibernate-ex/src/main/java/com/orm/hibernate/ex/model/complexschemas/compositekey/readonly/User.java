package com.orm.hibernate.ex.model.complexschemas.compositekey.readonly;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;
import java.util.Random;

//@Entity
@Table(name = "USERS")
public class User {
    @EmbeddedId
    private UserId id;
    @ManyToOne
    @JoinColumn(
            name = "DEPARTMENTID",
            insertable = false,
            updatable = false
    )
    private Department department;

    public User() {
    }

    public User(UserId id) {
        this.id = id;
    }

    public User(String username, Department department) {
        if (department.getId() == null)
            throw new IllegalStateException(
                    "Department is transient: " + department
            );
        this.id = new UserId(username, department.getId());
        this.department = department;
    }

    public UserId getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public static void main(String[] args) {
        final int genId = new Random().nextInt(10_000);
        final Department department = new Department("test-" + genId);

        QueryProcessor.process(entityManager -> {
            entityManager.persist(department);

            final UserId id = new UserId("John Doe [" + genId + "]", department.getId());
            final User user = new User(id);
            entityManager.persist(user);

            System.out.println("---> Department before save: " + user.getDepartment());
        });

        QueryProcessor.process(entityManager -> {
            final UserId userId = new UserId("John Doe [" + genId + "]", department.getId());
            final User user = entityManager.find(User.class, userId);
            System.out.println(user.getDepartment());
        });
    }
}
