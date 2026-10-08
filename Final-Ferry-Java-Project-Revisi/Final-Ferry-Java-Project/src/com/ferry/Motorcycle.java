package com.ferry;

/**
 * Class Motorcycle
 * Golongan II (<=500cc) atau Golongan III (>500cc dan roda tiga).
 * Golongannya ditentukan sendiri dari kapasitas mesin (engineCc).
 */
public class Motorcycle extends Vehicle {
    private int engineCc;

    public Motorcycle(String policeNumber, int engineCc) {
        super(policeNumber, categoryCodeFor(engineCc), descriptionFor(engineCc));
        this.engineCc = engineCc;
    }

    private static String categoryCodeFor(int engineCc) {
        return engineCc > 500 ? "3" : "2";
    }

    private static String descriptionFor(int engineCc) {
        return engineCc > 500
                ? "Sepeda motor diatas 500cc dan kendaraan roda tiga"
                : "Sepeda motor dibawah 500cc dan gerobak dorong";
    }

    public int getEngineCc() {
        return engineCc;
    }

    @Override
    public long getBaseTariff() {
        return engineCc > 500 ? 45_000L : 30_000L;
    }
}
