package com.ferry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class BookingFileStorage
 * Menyimpan dan membaca data Booking (beserta Ticket dan Payment) ke file teks.
 */
public class BookingFileStorage {
    private final String delimiter = "|";

    public BookingFileStorage() {
    }

    /** Menyimpan satu receipt ke akhir file receipts.txt. */
    public boolean saveReceipt(Receipt receipt, String filePath) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(receipt.toText());
            fw.write(System.lineSeparator());
            fw.write(System.lineSeparator());
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan receipt ke file: " + e.getMessage());
            return false;
        }
    }

    /** Menambahkan satu booking ke akhir file. Return true jika berhasil. */
    public boolean saveBooking(Booking booking, String filePath) {
        try (FileWriter fw = new FileWriter(filePath, true)) { // true = append
            fw.write(join("BOOKING", booking.getBookingId(), booking.getBookingDate()));
            fw.write(System.lineSeparator());

            for (Ticket t : booking.getTickets()) {
                Passenger p = t.getPassenger();
                Vehicle v = t.getVehicle();
                Route r = t.getRoute();
                Schedule s = t.getSchedule();
                Ferry f = t.getFerry();

                fw.write(join("TICKET",
                        t.getTicketNo(), String.valueOf(t.getTariff()),
                        p.getId(), p.getName(), p.getPhone(), p.getAddress(), p.getEmail(),
                        v == null ? "" : v.getPoliceNumber(),
                        v == null ? "" : v.getCategoryCode(),
                        v == null ? "" : v.getCategoryDescription(),
                        r.getRouteId(), r.getOrigin(), r.getDestination(),
                        s.getScheduleId(), s.getDepartureTime(), s.getCheckInTime(),
                        s.getServiceType().name(), String.valueOf(s.getDurationMinutes()),
                        f.getShipName(), f.getClassType(), String.valueOf(f.getDeck())));
                fw.write(System.lineSeparator());
            }

            Payment pay = booking.getPayment();
            if (pay != null) {
                BankAccount acc = pay.getBankAccount();
                fw.write(join("PAYMENT",
                        pay.getPaymentId(), pay.getMethod(), String.valueOf(pay.getAmount()),
                        pay.getPaymentTime(), acc.getBankName(), acc.getAccountNumber(),
                        acc.getAccountHolder()));
                fw.write(System.lineSeparator());
            }
            fw.write("END");
            fw.write(System.lineSeparator());
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan ke file: " + e.getMessage());
            return false;
        }
    }

    /** Membaca seluruh booking dari file. File belum ada = list kosong. */
    public List<Booking> loadBookings(String filePath) {
        List<Booking> result = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return result;
        }

        try (Scanner reader = new Scanner(file)) {
            Booking current = null;
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] f = line.split("\\|", -1); // -1 agar field kosong tetap terbaca
                try {
                    switch (f[0]) {
                        case "BOOKING":
                            current = new Booking(f[1], f[2]);
                            break;
                        case "TICKET":
                            if (current != null) {
                                current.addTicket(parseTicket(f));
                            }
                            break;
                        case "PAYMENT":
                            if (current != null) {
                                current.setPayment(parsePayment(f));
                            }
                            break;
                        case "END":
                            if (current != null) {
                                result.add(current);
                                current = null;
                            }
                            break;
                        default:
                            break;
                    }
                } catch (RuntimeException e) {
                    System.out.println("Baris rusak dilewati: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan: " + filePath);
        }
        return result;
    }

    private Ticket parseTicket(String[] f) {
        if (f.length != 22) {
            throw new IllegalArgumentException("Jumlah field TICKET tidak sesuai");
        }
        Passenger passenger = new Passenger(f[3], f[4], f[5], f[6], f[7]);
        Vehicle vehicle = f[8].isEmpty() ? null : Vehicle.fromCategoryCode(f[8], f[9]);
        Route route = new Route(f[11], f[12], f[13]);
        Schedule schedule = new Schedule(f[14], f[15], f[16],
                ServiceType.valueOf(f[17]), Integer.parseInt(f[18]));
        Ferry ferry = new Ferry(f[19], f[20], Integer.parseInt(f[21]));
        return new Ticket(f[1], passenger, vehicle, route, schedule, ferry,
                Long.parseLong(f[2]));
    }

    private Payment parsePayment(String[] f) {
        // 8 field (format baru); 7 field = file lama tanpa accountHolder
        if (f.length != 8 && f.length != 7) {
            throw new IllegalArgumentException("Jumlah field PAYMENT tidak sesuai");
        }
        String holder = (f.length == 8) ? f[7] : "-";
        BankAccount account = new BankAccount(f[5], f[6], holder);
        return new Payment(f[1], f[2], Long.parseLong(f[3]), f[4], account);
    }

    /** Menggabungkan field dengan pemisah "|" setelah dibersihkan. */
    private String join(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(clean(fields[i]));
        }
        return sb.toString();
    }

    /** Menghapus karakter yang dapat merusak format file. */
    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/").replace("\r", " ").replace("\n", " ");
    }
}
