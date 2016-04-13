package com.milan.recreationapp.xml;

import com.milan.recreationapp.model.ClubModel;
import com.milan.recreationapp.model.ClubSection;
import com.milan.recreationapp.model.ClubSectionBody;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ItemXMLHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    ClubModel clubModel = null;
    ClubSection clubSection = null;
    ClubSectionBody clubSectionBody = null;
    private ArrayList<ClubSectionBody> sectionBodyArrayList = new ArrayList<ClubSectionBody>();
    private ArrayList<ClubModel> clubList = new ArrayList<ClubModel>();
    private ArrayList<ClubSection> sectionList = new ArrayList<ClubSection>();

    public ArrayList<ClubModel> getClubList() {
        return clubList;
    }

    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("club")) {
            clubModel = new ClubModel();
        } else if (localName.equals("section"))
            clubSection = new ClubSection();
        else if(localName.equals("information"))
            sectionList=new ArrayList<>();
        else if(localName.equals("body"))
            sectionBodyArrayList=new ArrayList<>();
        else if (localName.equals("row"))
            clubSectionBody = new ClubSectionBody();

    }

    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("name"))
            clubModel.setName(currentValue);
        else if (localName.equalsIgnoreCase("address"))
            clubModel.setAddress(currentValue);
        else if (localName.equalsIgnoreCase("phoneNumber"))
            clubModel.setPhone(currentValue);
        else if (localName.equalsIgnoreCase("latitude"))
            clubModel.setLat(Double.parseDouble(currentValue));
        else if (localName.equalsIgnoreCase("longitude"))
            clubModel.setLng(Double.parseDouble(currentValue));
        else if (localName.equalsIgnoreCase("title")) {
            clubSection.setTitle(currentValue);
        } else if (localName.equalsIgnoreCase("days")) {
            if (clubSection.getTitle().equalsIgnoreCase("Opening hours") && currentValue.contains("24/7"))
                clubModel.setIs24Hour(true);
            clubSectionBody.setDays(currentValue);
        } else if (localName.equalsIgnoreCase("hours"))
            clubSectionBody.setHours(currentValue);
        else if (localName.equalsIgnoreCase("row"))
            sectionBodyArrayList.add(clubSectionBody);
        else if (localName.equalsIgnoreCase("section")) {
            clubSection.setClubSectionBodies(sectionBodyArrayList);
            sectionList.add(clubSection);
        } else if (localName.equalsIgnoreCase("club")) {
            clubModel.setClubSection(sectionList);
            clubList.add(clubModel);
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