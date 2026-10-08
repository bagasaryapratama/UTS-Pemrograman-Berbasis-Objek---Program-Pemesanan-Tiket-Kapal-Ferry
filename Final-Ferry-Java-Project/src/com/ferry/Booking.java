package com.ferry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class Booking
 */
public class Booking {
    private String bookingId;
    private List<Ticket> tickets;
    private Payment payment;
    private String bookingDate;
    private String status;

    public Booking(String bookingId, String bookingDate) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.tickets = new ArrayList<>();
        this.status = "UNPAID";
    }

    /** Menambahkan satu tiket ke booking. */
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    /** Menghubungkan object Payment setelah total booking diketahui. */
    public void setPayment(Payment payment) {
        this.payment = payment;
        this.status = "PAID";
    }

    /** Menghitung total seluruh tiket dalam satu booking. */
    public long getTotalAmount() {
        long total = 0;
        for (Ticket ticket : tickets) {
            total += ticket.getTariff();
        }
        return total;
    }

    public String getBookingId() {
        return bookingId;
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public Payment getPayment() {
        return payment;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }
}
