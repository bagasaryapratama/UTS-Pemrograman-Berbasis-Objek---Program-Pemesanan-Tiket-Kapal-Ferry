package com.ferry;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class Receipt
 * Mencetak ringkasan booking dan seluruh tiket di dalamnya.
 */
public class Receipt implements Printable {
    private String receiptNo;
    private Booking booking;
    private String printedAt;

    public Receipt(String receiptNo, Booking booking, String printedAt) {
        this.receiptNo = receiptNo;
        this.booking = booking;
        this.printedAt = printedAt;
    }

    public void cetak() {
        print();
    }

    @Override
    public void print() {
        System.out.println(toText());
    }

    /**
     * Menghasilkan isi receipt dalam bentuk String agar dapat
     * ditampilkan di console maupun disimpan ke file receipts.txt.
     */
    public String toText() {
        NumberFormat rupiah =
                NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append("====================================================================").append(System.lineSeparator());
        sb.append("                    BOARDING PASS / RECEIPT").append(System.lineSeparator());
        sb.append("                 SISTEM PEMESANAN TIKET FERRY").append(System.lineSeparator());
        sb.append("====================================================================").append(System.lineSeparator());
        sb.append("NO. BOOKING : ").append(booking.getBookingId()).append(System.lineSeparator());
        sb.append("NO. RECEIPT : ").append(receiptNo).append(System.lineSeparator());
        sb.append("TANGGAL     : ").append(booking.getBookingDate()).append(System.lineSeparator());
        sb.append("STATUS      : ").append(booking.getStatus()).append(System.lineSeparator());
        sb.append("--------------------------------------------------------------------").append(System.lineSeparator());
        sb.append("Jumlah tiket: ").append(booking.getTickets().size()).append(System.lineSeparator());
        sb.append(System.lineSeparator());

        int nomor = 1;
        for (Ticket ticket : booking.getTickets()) {
            Passenger passenger = ticket.getPassenger();
            Vehicle vehicle = ticket.getVehicle();
            Route route = ticket.getRoute();
            Schedule schedule = ticket.getSchedule();
            Ferry ferry = ticket.getFerry();

            // Gate & seat diambil dari BoardingPass agar sama dengan boarding pass yang dicetak.
            BoardingPass pass = new BoardingPass(
                    "BP" + booking.getBookingId() + "-" + nomor, ticket, "A", "");
            pass.setSeat(pass.generateSeat(nomor));

            sb.append("TIKET ").append(nomor).append(System.lineSeparator());
            sb.append("No. Tiket       : ").append(ticket.getTicketNo()).append(System.lineSeparator());
            sb.append("Nama Penumpang  : ").append(passenger.getName()).append(System.lineSeparator());
            sb.append("ID Penumpang    : ").append(passenger.getId()).append(System.lineSeparator());
            sb.append("Asal            : ").append(route.getOrigin()).append(System.lineSeparator());
            sb.append("Tujuan          : ").append(route.getDestination()).append(System.lineSeparator());
            sb.append("Layanan         : ").append(schedule.getServiceType().getDisplayName()).append(System.lineSeparator());
            sb.append("Durasi          : ").append(formatDuration(schedule.getDurationMinutes())).append(System.lineSeparator());
            sb.append("Kapal           : ").append(ferry.getShipName()).append(System.lineSeparator());
            sb.append("Kelas Kapal     : ").append(ferry.getClassType()).append(System.lineSeparator());
            sb.append("Deck            : ").append(ferry.getDeck()).append(System.lineSeparator());
            sb.append("Check-in        : ").append(schedule.getCheckInTime()).append(System.lineSeparator());
            sb.append("Keberangkatan   : ").append(schedule.getDepartureTime()).append(System.lineSeparator());

            if (vehicle == null) {
                sb.append("Kendaraan       : Tanpa kendaraan").append(System.lineSeparator());
            } else {
                sb.append("No. Polisi      : ").append(vehicle.getPoliceNumber()).append(System.lineSeparator());
                sb.append("Golongan        : ").append(vehicle.getCategoryCode()).append(System.lineSeparator());
                sb.append("Kategori        : ").append(vehicle.getCategoryDescription()).append(System.lineSeparator());
            }

            sb.append("Tarif           : ").append(rupiah.format(ticket.getTariff())).append(System.lineSeparator());
            sb.append("Gate            : ").append(pass.getGate()).append(System.lineSeparator());
            sb.append("Seat            : ").append(pass.getSeat()).append(System.lineSeparator());
            sb.append("--------------------------------------------------------------------").append(System.lineSeparator());
            nomor++;
        }

        sb.append("TOTAL PEMBAYARAN : ").append(rupiah.format(booking.getTotalAmount())).append(System.lineSeparator());
        if (booking.getPayment() != null) {
            sb.append("METODE BAYAR     : ").append(booking.getPayment().getMethod()).append(System.lineSeparator());
            sb.append("BANK             : ").append(booking.getPayment().getBankAccount().getBankName()).append(System.lineSeparator());
            sb.append("NO. REKENING     : ").append(booking.getPayment().getBankAccount().getAccountNumber()).append(System.lineSeparator());
            sb.append("WAKTU BAYAR      : ").append(booking.getPayment().getPaymentTime()).append(System.lineSeparator());
        }
        sb.append("Dicetak pada     : ").append(printedAt).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Catatan:").append(System.lineSeparator());
        sb.append("1. Datang sesuai waktu check-in.").append(System.lineSeparator());
        sb.append("2. Tunjukkan boarding pass saat proses naik kapal.").append(System.lineSeparator());
        sb.append("3. Simpan bukti pembayaran sampai perjalanan selesai.").append(System.lineSeparator());
        sb.append("====================================================================").append(System.lineSeparator());
        return sb.toString();
    }

    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return hours + " jam " + remainingMinutes + " menit";
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getPrintedAt() {
        return printedAt;
    }
}
