package com.milan.recreationapp.model;

import java.io.Serializable;

/**
 * Created by utsav.k on 06-04-2016.
 */
public class ClubClassDescriptionModel implements Serializable{
    private String name,description,location;


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
