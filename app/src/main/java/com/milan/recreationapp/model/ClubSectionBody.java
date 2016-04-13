package com.milan.recreationapp.model;

import java.io.Serializable;

/**
 * Created by utsav.k on 31-03-2016.
 */
public class ClubSectionBody implements Serializable {
    private String days, hours;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
