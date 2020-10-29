package com.orm.hibernate.ex.model.inheritance.associations.onetomany;

import com.orm.hibernate.ex.model.EntitySaver;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static User createUserWithBillingDetails(User user, List<BillingDetails> billingDetails) {
        EntitySaver.save(entityManager -> {
            billingDetails.forEach(bd -> {
                bd.setUser(user);
                entityManager.persist(bd);
            });
            user.getBillingDetails().addAll(billingDetails);
            entityManager.persist(user);
        });
        return user;
    }

    public static void main(String[] args) {
        User userBA = createUserWithBillingDetails(
                new User("John Doe"),
                Arrays.asList(
                        new BankAccount("123123123", "Citi Bank", "2020"),
                        new BankAccount("123123123", "Citi Bank", "2020"),
                        new BankAccount("123123123", "Citi Bank", "2020")
                )
        );

        EntitySaver.save(entityManager -> {
            final User uBA = entityManager.find(User.class, userBA.getId());
            System.out.println();
        });
    }
}
