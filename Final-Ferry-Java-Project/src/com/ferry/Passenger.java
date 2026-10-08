package com.ferry;

/**
 * Class Passenger
 * Menyimpan data penumpang dan mewarisi Person.
 */
public class Passenger extends Person {
    private String address;
    private String email;

    public Passenger(String id, String name, String phone,
                     String address, String email) {
        super(id, name, phone);
        this.address = address;
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
