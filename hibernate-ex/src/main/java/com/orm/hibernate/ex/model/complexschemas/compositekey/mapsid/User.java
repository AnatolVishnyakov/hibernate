package com.orm.hibernate.ex.model.complexschemas.compositekey.mapsid;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.*;

//@Entity
@Table(name = "USERS")
public class User {
    @EmbeddedId
    private UserId id;
    @ManyToOne
    @MapsId("departmentId") // перегрузка поля UserId.departmentId из Department.Id
    private Department department;

    public User() {
    }

    public User(UserId id) {
        this.id = id;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public static void main(String[] args) {
//        QueryProcessor.process(entityManager -> {
//            final Department department = new Department("test");
//            entityManager.persist(department);
//
//            final UserId id = new UserId("John Doe [" + new Random().nextInt(10_000) + "]", null);
//            final User user = new User(id);
//            user.setDepartment(department);
//            entityManager.persist(user);
//        });

        QueryProcessor.process(entityManager -> {
            final UserId id = new UserId("John Doe [6358]", 46L);
            final User user = entityManager.find(User.class, id);
            System.out.println(user.getId());
            System.out.println(user.getDepartment());
        });
    }
}
