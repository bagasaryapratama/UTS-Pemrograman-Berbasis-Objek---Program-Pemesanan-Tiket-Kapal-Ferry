package com.ferry;

/**
 * Class NonMotorVehicle
 * Golongan I: sepeda kayuh / gerobak tanpa motor.
 */
public class NonMotorVehicle extends Vehicle {

    public NonMotorVehicle(String policeNumber) {
        super(policeNumber, "1", "Sepeda Kayuh");
    }

    @Override
    public long getBaseTariff() {
        return 20_000L;
    }
}
