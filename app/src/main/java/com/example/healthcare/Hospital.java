package com.example.healthcare;

public class Hospital {
    String name;
    String contact;
    String address;
    String expertise;
    String otherDetails;

    public Hospital(String name, String contact, String address, String expertise, String otherDetails) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.expertise = expertise;
        this.otherDetails = otherDetails;
    }

    @Override
    public String toString() {
        return name; // used in ListView
    }
}
