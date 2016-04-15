package com.milan.recreationapp.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.database.DBHelper;
import com.milan.recreationapp.model.ClubClassDescriptionModel;
import com.milan.recreationapp.model.ClubDayTime;
import com.milan.recreationapp.model.ClubModel;
import com.milan.recreationapp.model.ClubSection;
import com.milan.recreationapp.model.ClubSectionBody;
import com.milan.recreationapp.model.ClubTimeTable;
import com.milan.recreationapp.util.Constant;
import com.milan.recreationapp.util.Utils;
import com.milan.recreationapp.xml.ItemXMLHandler;

import org.json.JSONArray;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by milanmanvar on 23/03/16.
 */
public class WelcomeScreen extends Activity implements View.OnClickListener {

    private Button btnChooseYourClub;
    private ReCreationApplication application;
    private ArrayList<ClubModel> clubList;
    private PopupMenu clubPopUp;
    private EditText etYourName;
    private TextView tvLetsGo;
    private DBHelper myDbHelper;
   // private ArrayList<String> clubs;
    private JSONArray jsClub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        application = (ReCreationApplication) this.getApplication();
        myDbHelper = application.getDatabase();
        myDbHelper.deleteTableData();
        btnChooseYourClub = (Button) findViewById(R.id.activity_welcome_et_choose_your_club);
        etYourName = (EditText) findViewById(R.id.activity_welcome_et_your_name);
        tvLetsGo = (TextView) findViewById(R.id.activity_welcome_tv_lets_go);

        btnChooseYourClub.setOnClickListener(this);
        tvLetsGo.setOnClickListener(this);
        clubList = new ArrayList<>();
        clubPopUp = new PopupMenu(this, btnChooseYourClub);

        downloadClubData();
        clubPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                btnChooseYourClub.setText(item.getTitle());
                SharedPreferences.Editor editor = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                editor.putString("club", item.getTitle().toString());
                editor.putInt("clubposition", item.getOrder());
                editor.commit();
                return true;
            }
        });
    }

    private void downloadClubData() {
        final ProgressDialog pd = ProgressDialog.show(WelcomeScreen.this, "", "Please wait", false, false);
        StringRequest reqDownloadClub = new StringRequest(Constant.clubUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response:", "" + response);
                SharedPreferences.Editor editor = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                editor.putString("clublist", response);
                editor.commit();
                parseClubListXML(response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                try {
                    callClubsDataApi();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Content-Type", "application/xml; charset=utf-8");

                return hashMap;
            }
        };
        application.addToRequestQueue(reqDownloadClub);
    }

    private void signUpApicall() {
        final ProgressDialog pd = ProgressDialog.show(WelcomeScreen.this, "", "Please wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.signupUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        Log.e("Sign up:", "" + response);
                        Intent intent = new Intent(WelcomeScreen.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        error.printStackTrace();
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String userGUid = UUID.randomUUID().toString();
                params.put("id", userGUid);
                params.put("fullName", etYourName.getText().toString().trim());
                params.put("selectedClubName", btnChooseYourClub.getText().toString());
                params.put("clubsFilter", jsClub.toString());

                Log.e("sign up req param:", "" + params.toString());
                SharedPreferences.Editor e = application.sharedPreferences.edit();
                e.putString("userguid", userGUid);
                e.commit();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
              //  headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                //Toast.makeText(WelcomeScreen.this,""+response.toString(),Toast.LENGTH_LONG).show();
                Log.e("status code",""+response.statusCode);
                return super.parseNetworkResponse(response);
            }

            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
        application.addToRequestQueue(stringRequest);
    }

    private void callAllClubsDataApi(String urlEnd, final int i) throws UnsupportedEncodingException {

        String url = Constant.clubDataUrl + urlEnd.replaceAll(" ", "%20") + ".xml";
        StringRequest reqDownloadClub = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences.Editor editor = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                editor.putString("club" + i, response);
                editor.commit();
//                AsyncStoreDataOnDb asyncStoreDataOnDb = new AsyncStoreDataOnDb(response);
//                asyncStoreDataOnDb.execute();
                try {
                    ArrayList<ClubTimeTable> clubTimeTables = Utils.parseClubDetailXML(response);
                    for (ClubTimeTable timeTable : clubTimeTables) {
                        ClubTimeTable clubTimeTable = timeTable;
                        for (ClubDayTime dayTime : clubTimeTable.getMorningClasses()) {
                            ClubDayTime clubDayTime = dayTime;
                            ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                            myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "morning", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                        }
                        for (ClubDayTime dayTime : clubTimeTable.getLunchtimeClasses()) {
                            ClubDayTime clubDayTime = dayTime;
                            ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                            myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "lunchtime", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                        }
                        for (ClubDayTime dayTime : clubTimeTable.getEveningClasses()) {
                            ClubDayTime clubDayTime = dayTime;
                            ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                            myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "evening", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", "" + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Content-Type", "application/xml; charset=utf-8");
                return hashMap;
            }
        };
        application.addToRequestQueue(reqDownloadClub);
    }

    private void callClubsDataApi() throws UnsupportedEncodingException {
        final ProgressDialog pd = ProgressDialog.show(WelcomeScreen.this, "", "Please wait", false, false);
        for (int i = 0; i < clubList.size(); i++) {
            String url = Constant.clubDataUrl + clubList.get(i).getName().replaceAll(" ", "%20") + ".xml";
            final int finalI = i;
            StringRequest reqDownloadClub = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    SharedPreferences.Editor editor = ((ReCreationApplication) getApplication()).sharedPreferences.edit();
                    editor.putString("club" + finalI, response);
                    editor.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ArrayList<ClubTimeTable> clubTimeTables = Utils.parseClubDetailXML(response);
                                for (ClubTimeTable timeTable : clubTimeTables) {
                                    ClubTimeTable clubTimeTable = timeTable;
                                    for (ClubDayTime dayTime : clubTimeTable.getMorningClasses()) {
                                        ClubDayTime clubDayTime = dayTime;
                                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "morning", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                                    }
                                    for (ClubDayTime dayTime : clubTimeTable.getLunchtimeClasses()) {
                                        ClubDayTime clubDayTime = dayTime;
                                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "lunchtime", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                                    }
                                    for (ClubDayTime dayTime : clubTimeTable.getEveningClasses()) {
                                        ClubDayTime clubDayTime = dayTime;
                                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());
                                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "evening", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if (finalI == clubList.size() - 1) {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error:", "" + error);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("Content-Type", "application/xml; charset=utf-8");
                    return hashMap;
                }
            };
            application.addToRequestQueue(reqDownloadClub);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_welcome_et_choose_your_club:
                if (clubList.size() > 0)
                    clubPopUp.show();
                break;

            case R.id.activity_welcome_tv_lets_go:

                if(etYourName.getText().toString().trim().length()>0) {
                    if(!btnChooseYourClub.getText().toString().equals(getString(R.string.choose_your_club_hint_txt))){
                        signUpApicall();
                    }else{
                        Utils.displayDialog("Whoops!","Please choose any club", WelcomeScreen.this);
                    }
                }else{
                    Utils.displayDialog("Whoops!","Please enter a name", WelcomeScreen.this);
                }

//                Intent intent = new Intent(WelcomeScreen.this, HomeScreen.class);
//                startActivity(intent);
//                finish();
                break;
        }

    }

    private void parseClubListXML(String xml) {

        String parsedData = "";

        try {
            /** Handling XML */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            ItemXMLHandler myXMLHandler = new ItemXMLHandler();
            xr.setContentHandler(myXMLHandler);
            InputSource inStream = new InputSource();

            inStream.setCharacterStream(new StringReader(xml));

            xr.parse(inStream);

            clubList = myXMLHandler.getClubList();
            int i = 0;
            //clubs = new ArrayList<>();
            jsClub = new JSONArray();
            for (ClubModel model : clubList) {
                ClubModel clubModel = model;
                for (ClubSection section : clubModel.getClubSection()) {
                    ClubSection clubSection = section;
                    for (ClubSectionBody body : clubSection.getClubSectionBodies()) {
                        ClubSectionBody clubSectionBody = body;
                        myDbHelper.insertClub(clubModel.getName(), clubModel.getAddress(), clubModel.getPhone(), clubModel.getLat(), clubModel.getLng(), clubSection.getTitle(), clubSectionBody.getDays(), clubSectionBody.getHours(), clubModel.is24Hour());
                    }
                }
//                callAllClubsDataApi(model.getName(), i + 1);
                clubPopUp.getMenu().add(i, i + 1, i + 1, model.getName());
                //clubs.add(model.getName());
                jsClub.put(model.getName());
                i = i + 1;
            }
            Log.w("AndroidParseXMLActivity", "Done");
        } catch (Exception e) {
            Log.w("AndroidParseXMLActivity", e);
        }


    }

    private class AsyncStoreDataOnDb extends AsyncTask<Void, Void, Void> {

        String response;

        AsyncStoreDataOnDb(String response) {
            this.response = response;
        }


        @Override
        protected Void doInBackground(Void... params) {
//            for (int clubC = 0; clubC < clubList.size(); clubC++) {
//                ClubModel clubModel = clubList.get(clubC);
//                for (ClubSection section : clubModel.getClubSection()) {
//                    ClubSection clubSection = section;
//                    for (ClubSectionBody body : clubSection.getClubSectionBodies()) {
//                        ClubSectionBody clubSectionBody = body;
//                        myDbHelper.insertClub(clubModel.getName(), clubModel.getAddress(), clubModel.getPhone(), clubModel.getLat(), clubModel.getLng(), clubSection.getTitle(), clubSectionBody.getDays(), clubSectionBody.getHours());
//                    }
//                }
            try {
                ArrayList<ClubTimeTable> clubTimeTables = Utils.parseClubDetailXML(response);
                for (ClubTimeTable timeTable : clubTimeTables) {
                    ClubTimeTable clubTimeTable = timeTable;
                    for (ClubDayTime dayTime : clubTimeTable.getMorningClasses()) {

                        ClubDayTime clubDayTime = dayTime;

                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());

                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "morning", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());

                    }

                    for (ClubDayTime dayTime : clubTimeTable.getLunchtimeClasses()) {

                        ClubDayTime clubDayTime = dayTime;

                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());

                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "lunchtime", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());

                    }

                    for (ClubDayTime dayTime : clubTimeTable.getEveningClasses()) {

                        ClubDayTime clubDayTime = dayTime;

                        ClubClassDescriptionModel clubClassDescriptionModel = Utils.getClubClassDescriptionModelArrayList().get(clubDayTime.getClassName());

                        myDbHelper.insertClubData(Utils.getClubName(), clubDayTime.getClassName(), clubDayTime.getInstructorName(), clubDayTime.getClassDuration(), clubDayTime.getClassTime(), clubTimeTable.getDay(), "evening", clubClassDescriptionModel.getDescription(), clubClassDescriptionModel.getLocation());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }


}
