package com.ferry;

/**
 * Class BoardingPass
 * Menyimpan data boarding untuk satu tiket
 */
public class BoardingPass implements Printable {
    private String boardingNo;
    private Ticket ticket;
    private String gate;
    private String seat;

    public BoardingPass(String boardingNo, Ticket ticket,
                        String gate, String seat) {
        this.boardingNo = boardingNo;
        this.ticket = ticket;
        this.gate = gate;
        this.seat = seat;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getBoardingNo() {
        return boardingNo;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getGate() {
        return gate;
    }

    public String getSeat() {
        return seat;
    }

    /** Nomor kursi bergilir sederhana, dipakai juga oleh Receipt. */
    public String generateSeat(int index) {
        return "A" + (10 + index);
    }

    @Override
    public void print() {
        System.out.println();
        System.out.println("---------------- BOARDING PASS ----------------");
        System.out.println("No. Boarding : " + boardingNo);
        System.out.println("No. Tiket    : " + ticket.getTicketNo());
        System.out.println("Penumpang    : " + ticket.getPassenger().getName());
        System.out.println("Rute         : " + ticket.getRoute().getOrigin()
                + " -> " + ticket.getRoute().getDestination());
        System.out.println("Kapal        : " + ticket.getFerry().getShipName());
        System.out.println("Berangkat    : " + ticket.getSchedule().getDepartureTime());
        System.out.println("Gate         : " + gate);
        System.out.println("Seat         : " + seat);
        System.out.println("-------------------------------------------------");
    }
}
