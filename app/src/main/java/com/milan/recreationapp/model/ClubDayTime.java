package com.milan.recreationapp.model;

import java.io.Serializable;

/**
 * Created by milanmanvar on 06/04/16.
 */
public class ClubDayTime implements Serializable{

    private String classTime;
    private String className;
    private String instructorName;
    private String classDuration;

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(String classDuration) {
        this.classDuration = classDuration;
    }
}
