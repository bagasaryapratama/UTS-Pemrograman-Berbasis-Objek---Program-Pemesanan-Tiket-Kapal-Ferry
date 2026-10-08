package com.ferry;

/**
 * Class Car
 * Mewakili golongan IV-A, V-A (kendaraan penumpang) dan V-B (kendaraan barang).
 * Golongan ditentukan dari tujuan pemakaian dan ukuran.
 */
public class Car extends Vehicle {
    private String purpose; // "Penumpang" atau "Barang"

    public Car(String policeNumber, String purpose, boolean under5m) {
        super(policeNumber, categoryCodeFor(purpose, under5m), descriptionFor(purpose, under5m));
        this.purpose = purpose;
    }

    private static String categoryCodeFor(String purpose, boolean under5m) {
        if (purpose.equalsIgnoreCase("Barang")) {
            return "5B";
        }
        return under5m ? "4A" : "5A";
    }

    private static String descriptionFor(String purpose, boolean under5m) {
        if (purpose.equalsIgnoreCase("Barang")) {
            return "Kendaraan barang (mobil/truk barang/tangki) <7 meter";
        }
        return under5m
                ? "Kendaraan penumpang (mobil/jeep/sedan/minicab/minibus/mikrolet/station wagon) <5 meter"
                : "Kendaraan penumpang (mobil/bus sedang) <7 meter";
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public long getBaseTariff() {
        switch (getCategoryCode()) {
            case "4A":
                return 60_000L;
            case "5A":
                return 85_000L;
            default: // 5B
                return 95_000L;
        }
    }
}
