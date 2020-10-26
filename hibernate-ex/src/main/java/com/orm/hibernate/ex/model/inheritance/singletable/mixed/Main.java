package com.orm.hibernate.ex.model.inheritance.singletable.mixed;

import com.orm.hibernate.ex.model.EntitySaver;

import javax.persistence.Query;

public class Main {
    public static void main(String[] args) {
        EntitySaver.save(entityManager -> {
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber("12345");
            creditCard.setExpMonth("11");
            creditCard.setExpYear("2020");
            creditCard.setOwner("cc owner");
            entityManager.persist(creditCard);

            final BankAccount bankAccount = new BankAccount();
            bankAccount.setAccount("test");
            bankAccount.setBankName("Bank of America");
            bankAccount.setSwift("ms");
            bankAccount.setOwner("ba owner");
            entityManager.persist(bankAccount);

            final Query query = entityManager.createQuery("select bd from BillingDetails bd");
            System.out.println(query.getResultList());
            System.out.println(entityManager.createQuery("select cc from CreditCard cc").getResultList());
            System.out.println(entityManager.createQuery("select ba from BankAccount ba").getResultList());
        });
    }
}
