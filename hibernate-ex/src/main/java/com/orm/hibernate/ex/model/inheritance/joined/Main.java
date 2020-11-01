package com.orm.hibernate.ex.model.inheritance.joined;

import com.orm.hibernate.ex.model.QueryProcessor;

import javax.persistence.Query;

public class Main {
    public static void main(String[] args) {
        QueryProcessor.process(entityManager -> {
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

            Query query = entityManager.createQuery("select bd from BillingDetails bd");
            System.out.println(query.getResultList());

            query = entityManager.createQuery("select cc from CreditCard cc");
            System.out.println(query.getResultList());

            query = entityManager.createQuery("select ba from BankAccount ba");
            System.out.println(query.getResultList());
        });
    }
}
