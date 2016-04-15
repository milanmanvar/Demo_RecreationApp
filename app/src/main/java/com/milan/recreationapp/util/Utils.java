package com.milan.recreationapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.milan.recreationapp.model.ClubClassDescriptionModel;
import com.milan.recreationapp.model.ClubModel;
import com.milan.recreationapp.model.ClubTimeTable;
import com.milan.recreationapp.xml.ItemXMLHandler;
import com.milan.recreationapp.xml.ItemXMLHandlerClubDetail;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by milanmanvar on 28/03/16.
 */
public class Utils {

    public static HashMap<String, ClubClassDescriptionModel> clubClassDescriptionModelArrayList;
    private static String clubName;

    /**
     * This function is used for keyboard hide
     **/
    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static ArrayList<ClubModel> parseClubListXML(String xml) {

        /*
        <root><notes>Completed</notes><lastUpdated>30-01-2012</lastUpdated>
        <clubInformation>
        <club>
        <name>Brighton East</name>
        <address>973 Nepean Highway, Moorabbin</address>
        <phoneNumber>9555 3290</phoneNumber>
        <latitude>-37.933027</latitude>
        <longitude>145.034483</longitude>
        <information>
        <section>
        <title>Opening Hours</title>
        <body>
        <row>
        <days>Open 24/7</days>
        <hours> </hours>
        </row>
        </body>
        </section>
        <section>
        <title>Our Staffed Hours</title>
        <body>
        <row>
        <days>Mon - Thu</days>
        <hours>7.30am - 8.00pm</hours>
        </row>
        <row>
        <days>Fri</days>
        <hours>7.30am - 6.30pm</hours>
        </row>
        <row>
        <days>Sat</days>
        <hours>7.30am - 5.00pm</hours>
        </row>
        <row>
        <days>Sun</days>
        <hours>8.00am - 2.00pm</hours>
        </row>
        </body>
        </section>
        <section>
        <title>Creche Hours</title>
        <body>
        <row>
        <days>Mon - Sat</days>
        <hours>8.25am - 12.00pm</hours>
        </row>
        </body>
        </section>
        </information>
        </club>
        </clubInformation></root>
         */
        ArrayList<ClubModel> clubList = new ArrayList<>();
        String parsedData = "";

        try {

            /** Handling XML */
            Log.e("Club xml", "" + xml);
            if (xml!=null && !xml.equalsIgnoreCase("")) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                ItemXMLHandler myXMLHandler = new ItemXMLHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();

                inStream.setCharacterStream(new StringReader(xml));

                xr.parse(inStream);

                clubList = myXMLHandler.getClubList();
            }
            Log.w("AndroidParseXMLActivity", "Done");
        } catch (Exception e) {
            Log.w("AndroidParseXMLActivity", e);
        }

        Log.e("Parsed data:", "" + parsedData);

        return clubList;
    }

    public static ArrayList<ClubTimeTable> parseClubDetailXML(String xml) {
        ArrayList<ClubTimeTable> clubTimeTables = new ArrayList<>();
        String parsedData = "";

        try {
            Log.e("XML detail:",""+xml);
            /** Handling XML */
            if (xml != null && !xml.equalsIgnoreCase("")) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                ItemXMLHandlerClubDetail myXMLHandler = new ItemXMLHandlerClubDetail();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();

                inStream.setCharacterStream(new StringReader(xml));
                xr.parse(inStream);
                setClubName(myXMLHandler.getClubName());
                clubTimeTables = myXMLHandler.getDays();
                setClubClassDescriptionModelArrayList(myXMLHandler.getClassesDesc());
            }
        } catch (Exception e) {
            Log.w("AndroidParseXMLActivity", e);
            Log.w("AndroidParseXML-xml", xml);
        }
        return clubTimeTables;

    }

    public static String getClubName() {
        return clubName;
    }

    public static void setClubName(String clubName) {
        Utils.clubName = clubName;
    }

    public static HashMap<String, ClubClassDescriptionModel> getClubClassDescriptionModelArrayList() {
        return clubClassDescriptionModelArrayList;
    }

    public static void setClubClassDescriptionModelArrayList(HashMap<String, ClubClassDescriptionModel> classesDesc) {
        clubClassDescriptionModelArrayList = classesDesc;
    }

    public static void displayDialog(String title, String msg, final Context context) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
