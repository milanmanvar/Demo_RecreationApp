package com.milan.recreationapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by utsav.k on 31-03-2016.
 */
public class ClubModel implements Serializable {

    private ArrayList<ClubSection> clubSection;
    private String name,address,phone;
    private double lat,lng;
    private boolean is24Hour;

    public boolean is24Hour() {
        return is24Hour;
    }

    public void setIs24Hour(boolean is24Hour) {
        this.is24Hour = is24Hour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<ClubSection> getClubSection() {
        return clubSection;
    }

    public void setClubSection(ArrayList<ClubSection> clubSection) {
        this.clubSection = clubSection;
    }
}
