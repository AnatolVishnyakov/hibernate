package com.orm.hibernate.ex.model.inheritance.associations;

import com.orm.hibernate.ex.model.EntitySaver;

public class Main {
    private static User createUserWithBillingDetails(User user, BillingDetails billingDetails) {
        EntitySaver.save(entityManager -> {
            user.setDefaultBilling(billingDetails);
            entityManager.persist(billingDetails);
            entityManager.persist(user);
        });
        return user;
    }

    public static void main(String[] args) {
        User userBA = createUserWithBillingDetails(
                new User("John Doe"),
                new BankAccount("123123123", "Citi Bank", "2020")
        );
        User userCC = createUserWithBillingDetails(
                new User("Mark Twen"),
                new CreditCard("0980988098", "04", "2021")
        );

        EntitySaver.save(entityManager -> {
            final User uBA = entityManager.find(User.class, userBA.getId());
            final BillingDetails defaultBillingBA = uBA.getDefaultBilling();
            System.out.println("BankAccountProxy instanceof BankAccount: " + (defaultBillingBA instanceof BankAccount)); // proxy
//            final BankAccount defaultBilling1 = (BankAccount) user.getDefaultBilling(); // ClassCastException

            final User uCC = entityManager.find(User.class, userCC.getId());
            final BillingDetails defaultBillingCC = uCC.getDefaultBilling();
            System.out.println("CreditCardProxy instanceof CreditCard: " + (defaultBillingCC instanceof CreditCard)); // proxy
        });

        EntitySaver.save(entityManager -> {
            final User user = entityManager.find(User.class, userBA.getId());
            final BillingDetails bd = user.getDefaultBilling();
            final CreditCard creditCard = entityManager.getReference(CreditCard.class, bd.getId());
            System.out.println(bd != creditCard); // difference proxy
        });

        EntitySaver.save(entityManager -> {
            final User user = (User) entityManager.createQuery(
                    "select u from User u " +
                            "left join fetch u.defaultBilling " +
                            "where u.id = :id")
                    .setParameter("id", userCC.getId())
                    .getSingleResult();
            final CreditCard creditCard = (CreditCard) user.getDefaultBilling();
            System.out.println(creditCard);
        });
    }
}
