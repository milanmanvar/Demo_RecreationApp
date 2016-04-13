package com.milan.recreationapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by utsav.k on 31-03-2016.
 */
public class ClubSection implements Serializable{
    private String title;
    private ArrayList<ClubSectionBody> clubSectionBodies;

    public ArrayList<ClubSectionBody> getClubSectionBodies() {
        return clubSectionBodies;
    }

    public void setClubSectionBodies(ArrayList<ClubSectionBody> clubSectionBodies) {
        this.clubSectionBodies = clubSectionBodies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
