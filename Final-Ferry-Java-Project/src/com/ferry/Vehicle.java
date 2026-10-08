package com.ferry;

/**
 * Abstract class Vehicle
 */
public abstract class Vehicle {
    private String policeNumber;
    private String categoryCode;
    private String categoryDescription;

    protected Vehicle(String policeNumber, String categoryCode, String categoryDescription) {
        this.policeNumber = policeNumber;
        this.categoryCode = categoryCode;
        this.categoryDescription = categoryDescription;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    /** Tarif dasar golongan ini (Reguler, sebelum dikali faktor rute/layanan). */
    public abstract long getBaseTariff();

    /**
     * Membangun kembali Vehicle dari categoryCode yang tersimpan di file.
     */
    public static Vehicle fromCategoryCode(String policeNumber, String categoryCode) {
        switch (categoryCode.toUpperCase()) {
            case "1":
                return new NonMotorVehicle(policeNumber);
            case "2":
                return new Motorcycle(policeNumber, 150);
            case "3":
                return new Motorcycle(policeNumber, 600);
            case "4A":
                return new Car(policeNumber, "Penumpang", true);
            case "5A":
                return new Car(policeNumber, "Penumpang", false);
            case "5B":
                return new Car(policeNumber, "Barang", false);
            case "6A":
                return new Bus(policeNumber);
            default:
                throw new IllegalArgumentException("Golongan tidak dikenal: " + categoryCode);
        }
    }
}
