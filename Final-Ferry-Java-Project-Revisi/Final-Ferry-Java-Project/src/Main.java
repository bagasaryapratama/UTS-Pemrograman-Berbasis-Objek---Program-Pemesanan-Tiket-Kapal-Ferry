import com.ferry.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Class Main
 * Program utama untuk sistem pemesanan tiket ferry.
 */
public class Main {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private static final String DATA_FILE = "bookings.txt";
    private static final String RECEIPT_FILE = "receipts.txt";

    private static final BookingFileStorage storage = new BookingFileStorage();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n================== MENU UTAMA ==================");
            System.out.println("1. Buat booking baru");
            System.out.println("2. Lihat booking tersimpan (dari file)");
            System.out.println("3. Keluar");
            int choice = readIntInRange(input, "Pilih menu (1-3): ", 1, 3);

            if (choice == 1) {
                createBooking(input);
            } else if (choice == 2) {
                showSavedBookings(input);
            } else {
                running = false;
            }
        }
        input.close();
    }

    /** Membaca booking dari file, menampilkan ringkasan, dan dapat mencetak receipt. */
    private static void showSavedBookings(Scanner input) {
        List<Booking> bookings = storage.loadBookings(DATA_FILE);
        if (bookings.isEmpty()) {
            System.out.println("Belum ada booking tersimpan di " + DATA_FILE + ".");
            return;
        }

        System.out.println("\nDAFTAR BOOKING TERSIMPAN");
        int no = 1;
        for (Booking b : bookings) {
            System.out.println(no + ". " + b.getBookingId()
                    + " | " + b.getBookingDate()
                    + " | " + b.getStatus()
                    + " | " + b.getTickets().size() + " tiket"
                    + " | " + formatRupiah(b.getTotalAmount()));
            no++;
        }

        int pick = readIntInRange(input,
                "Nomor booking untuk cetak receipt (0 = kembali): ", 0, bookings.size());
        if (pick > 0) {
            Booking b = bookings.get(pick - 1);
            Receipt receipt = new Receipt("RC" + b.getBookingId(), b,
                    LocalDateTime.now().format(DATE_TIME_FORMAT));
            printAll(receipt, b);
        }
    }

    /** Alur pembuatan satu booking baru, lalu disimpan ke file. */
    private static void createBooking(Scanner input) {
        System.out.println("===============================================================");
        System.out.println("          SISTEM PEMESANAN TIKET KAPAL FERRY");
        System.out.println("===============================================================");
        System.out.println("Tarif tiket dihitung otomatis oleh sistem.\n");

        // Kapal digunakan bersama oleh seluruh tiket dalam satu booking.
        Ferry ferry = new Ferry("EXPRESS IVA", "Ekonomi", 5);

        // Membuat object Booking untuk menampung seluruh tiket.
        String bookingId = "BK" + System.currentTimeMillis();
        String bookingDate = LocalDateTime.now().format(DATE_TIME_FORMAT);
        Booking booking = new Booking(bookingId, bookingDate);

        // User menentukan jumlah tiket dalam satu booking.
        int ticketCount = readPositiveInt(input,
                "Jumlah tiket yang ingin dipesan: ");

        // PENGATURAN YANG BERLAKU UNTUK SEMUA TIKET
        System.out.println("\n===============================================================");
        System.out.println("             DETAIL PERJALANAN (BERLAKU SEMUA)");
        System.out.println("===============================================================");

        // Kendaraan cukup dipilih satu kali untuk seluruh penumpang.
        Vehicle vehicle = readSharedVehicle(input);

        // Asal dan tujuan cukup dipilih satu kali.
        String origin = readPort(input, "ASAL");
        String destination = readPortDifferent(input, "TUJUAN", origin);
        Route route = new Route("R" + System.currentTimeMillis(), origin, destination);

        // Layanan (kelas) cukup dipilih satu kali.
        ServiceType serviceType = readServiceType(input);

        // Tanggal dan waktu keberangkatan cukup dipilih satu kali.
        LocalDateTime departure = readDeparture(input);
        LocalDateTime checkIn = departure.minusMinutes(60);
        Schedule schedule = new Schedule(
                "S" + System.currentTimeMillis(),
                departure.format(DATE_TIME_FORMAT),
                checkIn.format(DATE_TIME_FORMAT),
                serviceType,
                serviceType.getDurationMinutes()
        );

        System.out.println("\nKonfigurasi booking:");
        System.out.println("Asal          : " + origin);
        System.out.println("Tujuan        : " + destination);
        System.out.println("Layanan       : " + serviceType.getDisplayName());
        System.out.println("Keberangkatan : " + schedule.getDepartureTime());
        System.out.println("Check-in      : " + schedule.getCheckInTime());
        if (vehicle == null) {
            System.out.println("Kendaraan     : Tanpa kendaraan");
        } else {
            System.out.println("No. Polisi    : " + vehicle.getPoliceNumber());
            printVehicleInfo(vehicle);
        }

        // DATA SETIAP PENUMPANG
        for (int i = 1; i <= ticketCount; i++) {
            System.out.println("\n===============================================================");
            System.out.println("                     DATA PENUMPANG " + i);
            System.out.println("===============================================================");

            Passenger passenger = readPassenger(input, i);

            // Tarif dihitung otomatis berdasarkan konfigurasi bersama.
            long tariff = TariffService.calculateTariff(
                    route,
                    vehicle,
                    serviceType
            );

            String ticketNo = String.format("TKT-%s-%03d", bookingId, i);
            Ticket ticket = new Ticket(
                    ticketNo,
                    passenger,
                    vehicle,
                    route,
                    schedule,
                    ferry,
                    tariff
            );

            booking.addTicket(ticket);

            System.out.println("Tarif tiket " + i + " (otomatis) : " + formatRupiah(tariff));
        }

        // PEMBAYARAN TRANSFER BANK
        System.out.println("\n===============================================================");
        System.out.println("                       PEMBAYARAN");
        System.out.println("===============================================================");
        System.out.println("Jumlah tiket   : " + booking.getTickets().size());
        System.out.println("Total otomatis : " + formatRupiah(booking.getTotalAmount()));

        BankAccount bankAccount = readBankAccount(input);

        System.out.println("\nSilakan transfer sejumlah " + formatRupiah(booking.getTotalAmount()));
        System.out.println("Bank           : " + bankAccount.getBankName());
        System.out.println("No. Rekening   : " + bankAccount.getAccountNumber());
        System.out.println("a.n.           : " + bankAccount.getAccountHolder());

        String paymentTime = LocalDateTime.now().format(DATE_TIME_FORMAT);
        Payment payment = new Payment(
                "PAY" + System.currentTimeMillis(),
                "Transfer Bank",
                booking.getTotalAmount(),
                paymentTime,
                bankAccount
        );
        booking.setPayment(payment);

        // Cetak receipt untuk seluruh tiket dalam satu booking.
        Receipt receipt = new Receipt(
                "RC" + System.currentTimeMillis(),
                booking,
                LocalDateTime.now().format(DATE_TIME_FORMAT)
        );

        printAll(receipt, booking);

        // Simpan booking dan receipt ke file teks.
        boolean bookingSaved = storage.saveBooking(booking, DATA_FILE);
        boolean receiptSaved = storage.saveReceipt(receipt, RECEIPT_FILE);

        if (bookingSaved) {
            System.out.println("Booking tersimpan di file " + DATA_FILE + ".");
        }
        if (receiptSaved) {
            System.out.println("Receipt tersimpan di file " + RECEIPT_FILE + ".");
        }
    }

    /** Membaca data penumpang untuk setiap tiket. */
    private static Passenger readPassenger(Scanner input, int index) {
        String id = readNonEmpty(input, "ID Penumpang : ");
        String name = readNonEmpty(input, "Nama         : ");
        String phone = readNonEmpty(input, "No. Telepon  : ");
        String address = readNonEmpty(input, "Alamat       : ");
        String email = readNonEmpty(input, "Email        : ");

        return new Passenger(id, name, phone, address, email);
    }

    /**
     * Kendaraan hanya dipilih sekali dan digunakan oleh seluruh tiket.
     */
    private static Vehicle readSharedVehicle(Scanner input) {
        System.out.println("\n--- DATA KENDARAAN ---");
        String answer;
        while (true) {
            answer = readNonEmpty(input, "Apakah membawa kendaraan? (y/n): ");
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
                break;
            }
            System.out.println("Masukkan y untuk ya atau n untuk tidak.");
        }

        if (answer.equalsIgnoreCase("n")) {
            return null;
        }

        System.out.println("\nJenis Kendaraan:");
        System.out.println("1 = Sepeda / gerobak tanpa motor (Golongan I)");
        System.out.println("2 = Sepeda motor (Golongan II/III)");
        System.out.println("3 = Mobil (Golongan IV-A/V-A/V-B)");
        System.out.println("4 = Bus besar (Golongan VI-A)");
        int jenis = readIntInRange(input, "Pilih jenis kendaraan (1-4): ", 1, 4);
        String policeNumber = readNonEmpty(input, "No. Polisi : ");

        switch (jenis) {
            case 1:
                return new NonMotorVehicle(policeNumber);
            case 2:
                int cc = readPositiveInt(input, "Kapasitas mesin (cc): ");
                return new Motorcycle(policeNumber, cc);
            case 3:
                return readCar(input, policeNumber);
            default:
                return new Bus(policeNumber);
        }
    }

    /** Membaca detail Car: tujuan pemakaian dan ukuran (untuk menentukan golongan). */
    private static Car readCar(Scanner input, String policeNumber) {
        System.out.println("Tujuan pemakaian:");
        System.out.println("1 = Penumpang");
        System.out.println("2 = Barang");
        int purposeChoice = readIntInRange(input, "Pilih (1-2): ", 1, 2);
        String purpose = (purposeChoice == 1) ? "Penumpang" : "Barang";

        boolean under5m = true;
        if (purposeChoice == 1) {
            System.out.println("Ukuran kendaraan:");
            System.out.println("1 = Kurang dari 5 meter (Golongan IV-A)");
            System.out.println("2 = 5 sampai 7 meter (Golongan V-A)");
            int sizeChoice = readIntInRange(input, "Pilih (1-2): ", 1, 2);
            under5m = (sizeChoice == 1);
        }
        return new Car(policeNumber, purpose, under5m);
    }

    /** Menampilkan detail kendaraan + tarif dasarnya. */
    private static void printVehicleInfo(Vehicle vehicle) {
        System.out.println(" Golongan      : " + vehicle.getCategoryCode()
                + " (" + vehicle.getCategoryDescription() + ")");
        System.out.println(" Tarif dasar   : " + formatRupiah(vehicle.getBaseTariff()));
    }

    /** Menampilkan daftar pelabuhan dan membaca nomor pilihan. */
    private static String readPort(Scanner input, String label) {
        List<String> ports = PortData.getPorts();

        System.out.println("\nDaftar Pelabuhan - " + label + ":");
        for (int i = 0; i < ports.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, ports.get(i));
        }

        int selection = readIntInRange(
                input,
                "Pilih nomor pelabuhan " + label + ": ",
                1,
                ports.size()
        );
        return ports.get(selection - 1);
    }

    /** Membaca tujuan dan menolak tujuan yang sama dengan asal. */
    private static String readPortDifferent(Scanner input, String label, String origin) {
        List<String> ports = PortData.getPorts();

        System.out.println("\nDaftar Pelabuhan - " + label + ":");
        for (int i = 0; i < ports.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, ports.get(i));
        }

        while (true) {
            int selection = readIntInRange(
                    input,
                    "Pilih nomor pelabuhan " + label + ": ",
                    1,
                    ports.size()
            );
            String destination = ports.get(selection - 1);
            if (destination.equalsIgnoreCase(origin)) {
                System.out.println("Tujuan tidak boleh sama dengan asal. Pilih pelabuhan lain.");
            } else {
                return destination;
            }
        }
    }

    /** Menampilkan pilihan layanan yang berlaku untuk semua tiket. */
    private static ServiceType readServiceType(Scanner input) {
        System.out.println("\n--- PILIHAN LAYANAN / KELAS ---");
        System.out.println("1. Reguler - lebih murah, durasi " +
                formatDuration(ServiceType.REGULER.getDurationMinutes()));
        System.out.println("2. Express - lebih cepat, durasi " +
                formatDuration(ServiceType.EXPRESS.getDurationMinutes()) +
                ", tarif lebih mahal");

        int choice = readIntInRange(input, "Pilih layanan (1/2): ", 1, 2);
        return choice == 1 ? ServiceType.REGULER : ServiceType.EXPRESS;
    }

    /**
     * Membaca tanggal/waktu keberangkatan.
     * Hanya menit 00 atau 30 yang diperbolehkan.
     */
    private static LocalDateTime readDeparture(Scanner input) {
        while (true) {
            String text = readNonEmpty(input,
                    "Waktu keberangkatan (dd-MM-yyyy HH:mm) [hanya :00 atau :30]: ");
            try {
                LocalDateTime departure = LocalDateTime.parse(text, DATE_TIME_FORMAT);
                int minute = departure.getMinute();

                if (minute == 0 || minute == 30) {
                    return departure;
                }

                System.out.println("Waktu tidak valid. Menit harus 00 atau 30, misalnya 10:00 atau 10:30.");
            } catch (DateTimeParseException e) {
                System.out.println("Format salah. Contoh yang valid: 25-09-2026 19:30");
            }
        }
    }

    /** Menampilkan pilihan rekening bank tujuan transfer. */
    private static BankAccount readBankAccount(Scanner input) {
        System.out.println("\nPilih bank untuk transfer:");
        System.out.println("1. BCA");
        System.out.println("2. Mandiri");
        System.out.println("3. BNI");
        System.out.println("4. BRI");

        int choice = readIntInRange(input, "Pilih bank (1-4): ", 1, 4);
        switch (choice) {
            case 1:
                return new BankAccount("BCA", "1234567890", "PT Ferry Ticket Indonesia");
            case 2:
                return new BankAccount("Mandiri", "1300012345678", "PT Ferry Ticket Indonesia");
            case 3:
                return new BankAccount("BNI", "0123456789", "PT Ferry Ticket Indonesia");
            default:
                return new BankAccount("BRI", "020101234567890", "PT Ferry Ticket Indonesia");
        }
    }

    /** Membaca integer positif. */
    private static int readPositiveInt(Scanner input, String prompt) {
        while (true) {
            try {
                int value = Integer.parseInt(readNonEmpty(input, prompt));
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Masukkan angka lebih dari 0.");
        }
    }

    /** Membaca integer dalam rentang tertentu. */
    private static int readIntInRange(Scanner input, String prompt,
                                      int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readNonEmpty(input, prompt));
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Pilihan harus berupa angka " + min + " sampai " + max + ".");
        }
    }

    /** Membaca teks yang tidak boleh kosong. */
    private static String readNonEmpty(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input tidak boleh kosong.");
        }
    }

    /** Membuat satu BoardingPass untuk tiap tiket dalam booking. */
    private static List<BoardingPass> buildBoardingPasses(Booking booking) {
        List<BoardingPass> passes = new ArrayList<>();
        int nomor = 1;
        for (Ticket ticket : booking.getTickets()) {
            BoardingPass pass = new BoardingPass(
                    "BP" + booking.getBookingId() + "-" + nomor,
                    ticket, "A", "");
            pass.setSeat(pass.generateSeat(nomor));
            passes.add(pass);
            nomor++;
        }
        return passes;
    }

    /** Mencetak Receipt lalu semua BoardingPass-nya lewat satu daftar. */
    private static void printAll(Receipt receipt, Booking booking) {
        List<Printable> printables = new ArrayList<>();
        printables.add(receipt);
        printables.addAll(buildBoardingPasses(booking));
        for (Printable p : printables) {
            p.print();
        }
    }

    /** Format rupiah sederhana untuk output console. */
    private static String formatRupiah(long amount) {
        return String.format("Rp%,d", amount).replace(',', '.');
    }

    /** Format durasi ke jam dan menit. */
    private static String formatDuration(int minutes) {
        int hours = minutes / 60;
        int remaining = minutes % 60;
        return hours + " jam " + remaining + " menit";
    }
}
