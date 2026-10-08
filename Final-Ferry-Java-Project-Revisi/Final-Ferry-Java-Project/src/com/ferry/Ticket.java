package com.ferry;

/**
 * Class Ticket
 * Satu object Ticket mewakili satu tiket penumpang.
 */
public class Ticket {
    private String ticketNo;
    private Passenger passenger;
    private Vehicle vehicle;
    private Route route;
    private Schedule schedule;
    private Ferry ferry;
    private long tariff;

    public Ticket(String ticketNo, Passenger passenger, Vehicle vehicle,
                  Route route, Schedule schedule, Ferry ferry,
                  long tariff) {
        this.ticketNo = ticketNo;
        this.passenger = passenger;
        this.vehicle = vehicle;
        this.route = route;
        this.schedule = schedule;
        this.ferry = ferry;
        this.tariff = tariff;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Route getRoute() {
        return route;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Ferry getFerry() {
        return ferry;
    }

    public long getTariff() {
        return tariff;
    }
}
