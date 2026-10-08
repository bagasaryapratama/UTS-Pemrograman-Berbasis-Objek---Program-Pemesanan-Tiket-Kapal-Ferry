package com.ferry;

/**
 * Class Payment
 * Menyimpan data pembayaran.
 * Nominal diambil otomatis dari total Booking.
 */
public class Payment {
    private String paymentId;
    private String method;
    private long amount;
    private String paymentTime;
    private BankAccount bankAccount;

    public Payment(String paymentId, String method, long amount,
                   String paymentTime, BankAccount bankAccount) {
        this.paymentId = paymentId;
        this.method = method;
        this.amount = amount;
        this.paymentTime = paymentTime;
        this.bankAccount = bankAccount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getMethod() {
        return method;
    }

    public long getAmount() {
        return amount;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
