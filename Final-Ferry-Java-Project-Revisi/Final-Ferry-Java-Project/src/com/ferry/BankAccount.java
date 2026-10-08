package com.ferry;

/**
 * Class BankAccount
 * Menyimpan rekening tujuan transfer yang ditampilkan kepada user.
 */
public class BankAccount {
    private String bankName;
    private String accountNumber;
    private String accountHolder;

    public BankAccount(String bankName, String accountNumber, String accountHolder) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }
}
