package com.orm.hibernate.ex.model.inheritance.joined;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

//@Entity
@PrimaryKeyJoinColumn(name = "BANKACCOUNT_ID")
public class BankAccount extends BillingDetails {
    @NotNull
    private String account;
    @NotNull
    private String bankName;
    @NotNull
    private String swift;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "account='" + account + '\'' +
                ", bankName='" + bankName + '\'' +
                ", swift='" + swift + '\'' +
                '}';
    }
}
