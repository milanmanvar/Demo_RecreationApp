package com.milan.recreationapp.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.ClubTimeTable_New;
import com.milan.recreationapp.util.Constant;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by utsav.k on 06-04-2016.
 */
public class ClubClassDetailActivity extends BaseActivity {

    private TextView txtTime, txtDuration, txtInst, txtClub, txtLocation, txtDesc;
    private Button btnSave;
    private ReCreationApplication reCreationApplication;
    //    private ClubDayTime clubDayTime;
    private ClubTimeTable_New clubDayTime;
    //        private ClubClassDescriptionModel clubClassDescriptionModel;
    private TextView txtLblSaved;
    private ImageView imgFb, imgTwitter;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);
        shareDialog = new ShareDialog(this);
        txtClub = (TextView) findViewById(R.id.class_detail_txtClub);
        txtDesc = (TextView) findViewById(R.id.class_detail_txtDesc);
        txtDuration = (TextView) findViewById(R.id.class_detail_txtDuration);
        txtInst = (TextView) findViewById(R.id.class_detail_txtInst);
        txtLocation = (TextView) findViewById(R.id.class_detail_txtLocation);
        txtTime = (TextView) findViewById(R.id.class_detail_txtTime);
        btnSave = (Button) findViewById(R.id.class_detail_btnSaveToMyClass);
        txtLblSaved = (TextView) findViewById(R.id.class_detail_txtLblSaved);
        imgFb = (ImageView) findViewById(R.id.class_detail_imgFb);
        imgTwitter = (ImageView) findViewById(R.id.class_detail_imgTwitter);

        txtClub.setText(((ReCreationApplication) getApplication()).sharedPreferences.getString("club", ""));
        reCreationApplication = (ReCreationApplication) getApplication();
        if (this.getIntent().hasExtra("clubdaytime")) {

            clubDayTime = (ClubTimeTable_New) this.getIntent().getSerializableExtra("clubdaytime");
            setUpActionBar(clubDayTime.getClassName());
            txtDuration.setText(clubDayTime.getDuration());
            txtInst.setText(clubDayTime.getInstructor());
            txtTime.setText(clubDayTime.getTime());
            txtDesc.setText(clubDayTime.getDesc());
            txtLocation.setText(clubDayTime.getLocation());
            if (clubDayTime.getIsSaved() == 1) {
                btnSave.setVisibility(View.GONE);
                txtLblSaved.setVisibility(View.VISIBLE);
            } else {
                btnSave.setVisibility(View.VISIBLE);
                txtLblSaved.setVisibility(View.GONE);
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reCreationApplication.getDatabase().saveToMyClass(clubDayTime.getId());
                    btnSave.setVisibility(View.GONE);
                    txtLblSaved.setVisibility(View.VISIBLE);
                    //confirmationMessage();
                    createGymClassApicall();
                }
            });
            if (clubDayTime.getEventId() == 0) {
                txtLblSaved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmationMessage();
                    }
                });
            }
        }
        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class))
                    shareDialog.show(content);
            }
        });
        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = null;
                try {
                    builder = new TweetComposer.Builder(ClubClassDetailActivity.this).text(getString(R.string.app_name)).url(new URL("https://www.google.com"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.show();
            }
        });

    }


    private void createGymClassApicall() {
        final ProgressDialog pd = ProgressDialog.show(ClubClassDetailActivity.this, "", "Please wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.saveMyClassUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pd != null && pd.isShowing())
                            pd.dismiss();

                        confirmationMessage();

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
                String id = UUID.randomUUID().toString();
                params.put("id", id);

                params.put("userId", reCreationApplication.sharedPreferences.getString("userguid",""));
                params.put("name", clubDayTime.getClassName());
                params.put("clubName", reCreationApplication.sharedPreferences.getString("club",""));
                params.put("duration", clubDayTime.getDuration());
                params.put("location", clubDayTime.getLocation());
                params.put("instructor", clubDayTime.getInstructor());
                params.put("gymClassDescription", clubDayTime.getDesc());


                Log.e("create class param:", "" + params.toString());

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
        reCreationApplication.addToRequestQueue(stringRequest);
    }

    private void confirmationMessage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Successfully added to My Classes. Would you like to be alerted before this class starts?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent iAlert = new Intent(ClubClassDetailActivity.this, AlertClassActivity.class);
                iAlert.putExtra("clubalert", clubDayTime);
                startActivity(iAlert);
            }
        });
        builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
