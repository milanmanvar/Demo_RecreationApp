package com.milan.recreationapp.xml;

import com.milan.recreationapp.model.ClubClassDescriptionModel;
import com.milan.recreationapp.model.ClubDayTime;
import com.milan.recreationapp.model.ClubTimeTable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemXMLHandlerClubDetail extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    String clubName = "";
    String lastUpdated = "";

    ClubDayTime clubDayTime = new ClubDayTime();
    ClubClassDescriptionModel clubClassDescriptionModel = new ClubClassDescriptionModel();
    ClubTimeTable clubTimeTable = new ClubTimeTable();

    int statusOfDay;

//    ClubTimeTable monday = new ClubTimeTable();
//    ClubTimeTable tuesday = new ClubTimeTable();
//    ClubTimeTable wednesday = new ClubTimeTable();
//    ClubTimeTable thursday = new ClubTimeTable();
//    ClubTimeTable friday = new ClubTimeTable();
//    ClubTimeTable saturday = new ClubTimeTable();
//    ClubTimeTable sunday = new ClubTimeTable();

    ArrayList<ClubTimeTable> days = new ArrayList<ClubTimeTable>();
    //    ArrayList<ClubClassDescriptionModel> classes = new ArrayList<ClubClassDescriptionModel>();
    HashMap<String, ClubClassDescriptionModel> classDescHash = new HashMap<>();

    public String getClubName(){
        return clubName;
    }
    public ArrayList<ClubTimeTable> getDays() {
        return days;
    }

    public HashMap<String, ClubClassDescriptionModel> getClassesDesc() {
        return classDescHash;
    }

    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("class")) {
            if (statusOfDay == 4)
                clubClassDescriptionModel = new ClubClassDescriptionModel();
            else
                clubDayTime = new ClubDayTime();
        } else if (localName.equals("monday") || localName.equals("tuesday") || localName.equals("wednesday") || localName.equals("thursday") || localName.equals("friday") || localName.equals("saturday") || localName.equals("sunday")) {
            clubTimeTable = new ClubTimeTable();
            clubTimeTable.setDay(localName);
        } else if (localName.equals("morningClasses")) {
            statusOfDay = 1;
        } else if (localName.equals("lunchtimeClasses")) {
            statusOfDay = 2;
        } else if (localName.equals("eveningClasses")) {
            statusOfDay = 3;
        } else if (localName.equals("classDescriptions")) {
            statusOfDay = 4;
        }

    }

    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("clubName"))
            clubName = currentValue;
        else if (localName.equalsIgnoreCase("lastUpdated"))
            lastUpdated = currentValue;
        else if (localName.equalsIgnoreCase("time"))
            clubDayTime.setClassTime(currentValue);
        else if (localName.equalsIgnoreCase("name")) {
            if (statusOfDay == 4)
                clubClassDescriptionModel.setName(currentValue);
            else
                clubDayTime.setClassName(currentValue);
        } else if (localName.equalsIgnoreCase("instructor"))
            clubDayTime.setInstructorName(currentValue);
        else if (localName.equalsIgnoreCase("duration"))
            clubDayTime.setClassDuration(currentValue);
        else if (localName.equalsIgnoreCase("description"))
            clubClassDescriptionModel.setDescription(currentValue);
        else if (localName.equalsIgnoreCase("location"))
            clubClassDescriptionModel.setLocation(currentValue);
        else if (localName.equalsIgnoreCase("class")) {

            if (statusOfDay == 1) {
                clubTimeTable.getMorningClasses().add(clubDayTime);
            } else if (statusOfDay == 2) {
                clubTimeTable.getLunchtimeClasses().add(clubDayTime);
            } else if (statusOfDay == 3) {
                clubTimeTable.getEveningClasses().add(clubDayTime);
            } else if (statusOfDay == 4)
                classDescHash.put(clubClassDescriptionModel.getName(), clubClassDescriptionModel);

        } else if (localName.equals("morningClasses") || localName.equals("lunchtimeClasses") || localName.equals("eveningClasses") || localName.equals("classDescriptions")) {
            statusOfDay = 0;
        } else if (localName.equals("monday") || localName.equals("tuesday") || localName.equals("wednesday") || localName.equals("thursday") || localName.equals("friday") || localName.equals("saturday") || localName.equals("sunday")) {
            days.add(clubTimeTable);
        }

    }

    // Called to get tag characters 
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }

    }

}