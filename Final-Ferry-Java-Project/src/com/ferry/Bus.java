package com.ferry;

/**
 * Class Bus
 * Golongan VI-A: bus besar / kendaraan penumpang <10 meter.
 */
public class Bus extends Vehicle {

    public Bus(String policeNumber) {
        super(policeNumber, "6A", "Kendaraan penumpang (mobil, bus besar) <10 meter");
    }

    @Override
    public long getBaseTariff() {
        return 120_000L;
    }
}
