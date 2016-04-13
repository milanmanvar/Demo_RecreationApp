package com.milan.recreationapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.ReCreationApplication;
import com.milan.recreationapp.model.ClubTimeTable_New;

import java.util.ArrayList;

/**
 * Created by utsav.k on 06-04-2016.
 */
public class ClubTimeTableDetailActivity extends BaseActivity {

    private LinearLayout lTimeTable;
    private ClubTimeTable_New clubTimeTable;
    private ArrayList<ClubTimeTable_New> list;
    private ReCreationApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_timetable_detail);
        lTimeTable = (LinearLayout) findViewById(R.id.clubtimetable_detail_l);
        application = ((ReCreationApplication) getApplication());
        setUpActionBar(application.sharedPreferences.getString("club", ""));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.getIntent().hasExtra("timetable")) {
            clubTimeTable = (ClubTimeTable_New) this.getIntent().getSerializableExtra("timetable");
            list = application.getDatabase().getClubTimeTableDetailFromNameAndDay(clubTimeTable.getClubName(), clubTimeTable.getDay());
            String temp = "";
            lTimeTable.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                View vMorning = getLayoutInflater().inflate(R.layout.row_timetable_title, null);
                TextView txtTitle = (TextView) vMorning.findViewById(R.id.row_timetable_txtTitle);

                if (list.get(i).getClassType().toString().trim().contains("morning")) {
                    txtTitle.setText("Morning Classes");
                    txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_morning, 0, 0, 0);
                } else if (list.get(i).getClassType().toString().trim().contains("lunchtime")) {
                    txtTitle.setText("Lunchtime Classes");
                    txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lunchtime, 0, 0, 0);
                } else if (list.get(i).getClassType().toString().trim().contains("evening")) {
                    txtTitle.setText("Evening Classes");
                    txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_evening, 0, 0, 0);
                }
                if (!temp.equalsIgnoreCase(txtTitle.getText().toString().trim()))
                    lTimeTable.addView(vMorning);
                temp = txtTitle.getText().toString().trim();

                View vBody = getLayoutInflater().inflate(R.layout.row_timetable, null);
                TextView txtName = (TextView) vBody.findViewById(R.id.row_timetable_txtName);
                txtName.setText(list.get(i).getTime() + " " + list.get(i).getClassName());
                ImageView imgCheck = (ImageView) vBody.findViewById(R.id.row_timetable_imgCheck);
                imgCheck.setVisibility(list.get(i).getIsSaved() == 1 ? View.VISIBLE : View.INVISIBLE);
                final int finalI = i;
                vBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iClass = new Intent(ClubTimeTableDetailActivity.this, ClubClassDetailActivity.class);
                        iClass.putExtra("clubdaytime", list.get(finalI));
                        startActivity(iClass);
                    }
                });
                lTimeTable.addView(vBody);
            }


//            View vMorning = getLayoutInflater().inflate(R.layout.row_timetable_title, null);
//            TextView txtTitle = (TextView) vMorning.findViewById(R.id.row_timetable_txtTitle);
//            txtTitle.setText("Morning Classes");
//            if (clubTimeTable.getMorningClasses() != null && clubTimeTable.getMorningClasses().size() > 0) {
//                lTimeTable.addView(vMorning);
//                for (int i = 0; i < clubTimeTable.getMorningClasses().size(); i++) {
//                    final ClubDayTime dayTime = clubTimeTable.getMorningClasses().get(i);
//                    View vBody = getLayoutInflater().inflate(R.layout.row_timetable, null);
//                    TextView txtName = (TextView) vBody.findViewById(R.id.row_timetable_txtName);
//                    txtName.setText(dayTime.getClassTime() + " " + dayTime.getClassName());
//                    vBody.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent iClass = new Intent(ClubTimeTableDetailActivity.this, ClubClassDetailActivity.class);
//                            iClass.putExtra("clubdaytime", dayTime);
//                            startActivity(iClass);
//                        }
//                    });
//                    lTimeTable.addView(vBody);
//                }
//            }
//            View vLunch = getLayoutInflater().inflate(R.layout.row_timetable_title, null);
//            TextView txtLunchTitle = (TextView) vLunch.findViewById(R.id.row_timetable_txtTitle);
//            txtLunchTitle.setText("Lunchtime Classes");
//            if (clubTimeTable.getLunchtimeClasses() != null && clubTimeTable.getLunchtimeClasses().size() > 0) {
//                lTimeTable.addView(vLunch);
//                for (int i = 0; i < clubTimeTable.getLunchtimeClasses().size(); i++) {
//                    final ClubDayTime dayTime = clubTimeTable.getLunchtimeClasses().get(i);
//                    View vBody = getLayoutInflater().inflate(R.layout.row_timetable, null);
//                    TextView txtName = (TextView) vBody.findViewById(R.id.row_timetable_txtName);
//                    txtName.setText(dayTime.getClassTime() + " " + dayTime.getClassName());
//                    vBody.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent iClass = new Intent(ClubTimeTableDetailActivity.this, ClubClassDetailActivity.class);
//                            iClass.putExtra("clubdaytime", dayTime);
//                            startActivity(iClass);
//                        }
//                    });
//                    lTimeTable.addView(vBody);
//                }
//            }
//            View vEvening = getLayoutInflater().inflate(R.layout.row_timetable_title, null);
//            TextView txtEveningTitle = (TextView) vEvening.findViewById(R.id.row_timetable_txtTitle);
//            txtEveningTitle.setText("Evening Classes");
//            if (clubTimeTable.getEveningClasses() != null && clubTimeTable.getEveningClasses().size() > 0) {
//                lTimeTable.addView(vEvening);
//                for (int i = 0; i < clubTimeTable.getEveningClasses().size(); i++) {
//                    final ClubDayTime dayTime = clubTimeTable.getEveningClasses().get(i);
//                    View vBody = getLayoutInflater().inflate(R.layout.row_timetable, null);
//                    TextView txtName = (TextView) vBody.findViewById(R.id.row_timetable_txtName);
//                    txtName.setText(dayTime.getClassTime() + " " + dayTime.getClassName());
//                    vBody.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent iClass = new Intent(ClubTimeTableDetailActivity.this, ClubClassDetailActivity.class);
//                            iClass.putExtra("clubdaytime", dayTime);
//                            startActivity(iClass);
//                        }
//                    });
//                    lTimeTable.addView(vBody);
//                }
//            }
        }
    }
}
