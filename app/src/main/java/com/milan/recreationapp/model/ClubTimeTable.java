package com.milan.recreationapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by milanmanvar on 06/04/16.
 */
public class ClubTimeTable implements Serializable{

    private ArrayList<ClubDayTime> morningClasses = new ArrayList<ClubDayTime>();
    private ArrayList<ClubDayTime> lunchtimeClasses = new ArrayList<ClubDayTime>();
    private ArrayList<ClubDayTime> eveningClasses = new ArrayList<ClubDayTime>();
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<ClubDayTime> getMorningClasses() {
        return morningClasses;
    }

    public void setMorningClasses(ArrayList<ClubDayTime> morningClasses) {
        this.morningClasses = morningClasses;
    }

    public ArrayList<ClubDayTime> getLunchtimeClasses() {
        return lunchtimeClasses;
    }

    public void setLunchtimeClasses(ArrayList<ClubDayTime> lunchtimeClasses) {
        this.lunchtimeClasses = lunchtimeClasses;
    }

    public ArrayList<ClubDayTime> getEveningClasses() {
        return eveningClasses;
    }

    public void setEveningClasses(ArrayList<ClubDayTime> eveningClasses) {
        this.eveningClasses = eveningClasses;
    }
}
